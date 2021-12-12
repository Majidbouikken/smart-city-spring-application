package com.smartcity.springapplication.offer

import com.smartcity.springapplication.category.Category
import com.smartcity.springapplication.notification.Notification
import lombok.AllArgsConstructor
import com.smartcity.springapplication.notification.service.NotificationService
import com.smartcity.springapplication.user.simpleUser.SimpleUserService
import com.smartcity.springapplication.product.ProductMapper
import com.smartcity.springapplication.utils.error_handler.DateException
import com.smartcity.springapplication.user.simpleUser.SimpleUser
import java.util.concurrent.Executors
import com.smartcity.springapplication.notification.NotificationType
import com.smartcity.springapplication.utils.error_handler.OfferException
import com.smartcity.springapplication.product.Product
import com.smartcity.springapplication.productVariant.ProductVariant
import com.smartcity.springapplication.product.ProductDTO
import com.smartcity.springapplication.store.Store
import com.smartcity.springapplication.utils.DateUtil
import com.smartcity.springapplication.utils.Response
import com.smartcity.springapplication.utils.pair.Pair
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier
import java.util.stream.Collectors
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Service
@AllArgsConstructor
class OfferService @Autowired constructor(
    val offerRepository: OfferRepository,
    val notificationService: NotificationService,
    val simpleUserService: SimpleUserService,
) {
    private val offerMapper: OfferMapper? = null
    private val productMapper: ProductMapper? = null

    @Transactional
    fun createOffer(offerCreationDTO: OfferCreationDTO): Response<String> {
        if (!DateUtil.isValidDate(offerCreationDTO.startDate) || !DateUtil.isValidDate(offerCreationDTO.endDate)) {
            throw DateException("error.date.invalid")
        }
        offerCreationDTO.id = null
        Optional.of(offerCreationDTO)
            .map { offerCreationDto: OfferCreationDTO? -> offerMapper!!.toModel(offerCreationDto) }
            .map { offer: Offer? -> SetOffer.apply(offer).apply(GetOfferTypes.get()) }
            .map { entity: Offer -> offerRepository.save(entity) }
            .map { offer: Offer -> prepareNotification(offer) }
            .map { offer: Offer -> setOffersUser(offer) }
        return Response("created.")
    }

    private fun setOffersUser(offer: Offer): Offer {
        simpleUserService.findSimpleUserByInterestCenterAndAround(offer.store!!)!!
            .forEach(Consumer { user: SimpleUser? -> simpleUserService.setOffers(user!!, offer) })
        return offer
    }

    private fun prepareNotification(offer: Offer): Offer {
        val threadPool = Executors.newCachedThreadPool()
        offer.store!!.defaultCategories
            .forEach(Consumer { category: Category -> threadPool.submit { sendNotification(category, offer) } })
        threadPool.shutdown()
        return offer
    }

    private fun sendNotification(category: Category, offer: Offer) {
        when (offer.type) {
            OfferType.FIXED -> notificationService.sendNotification(
                Notification(
                    "New Discount",
                    "Check " + "-" + offer.newPrice + "$" + " off at " + offer.store!!.name + ", start on " + DateUtil.parseStringSimpleFormat(
                        offer.startDate
                    ),
                    NotificationType.DISCOUNT,
                    category.name,
                    null
                )
            )
            OfferType.PERCENTAGE -> notificationService.sendNotification(
                Notification(
                    "New discount",
                    "Check " + "-" + offer.percentage + "%" + " off at " + offer.store!!.name + ", start on " + DateUtil.parseStringSimpleFormat(
                        offer.startDate
                    ),
                    NotificationType.DISCOUNT,
                    category.name,
                    null
                )
            )
        }
    }

    @Transactional
    fun deleteOffer(id: Long?): Response<String> {
        val offer = offerRepository.findById(id!!)
            .orElseThrow { OfferException("error.offer.notFound") }!!
        offer.deleted = true
        offerRepository.save(offer)
        return Response("deleted.")
    }

    fun updateOffer(offerCreationDTO: OfferCreationDTO): Response<String> {
        if (!DateUtil.isValidDate(offerCreationDTO.startDate) || !DateUtil.isValidDate(offerCreationDTO.endDate)) {
            throw DateException("error.date.invalid")
        }
        val oldOffer = offerRepository.findById(offerCreationDTO.id!!)
            .orElseThrow { OfferException("error.offer.notFound") }!!
        deleteOffer(oldOffer.id)
        Optional.of(offerCreationDTO)
            .map { offerCreationDTO: OfferCreationDTO? -> offerMapper!!.toModel(offerCreationDTO) }
            .map { offer: Offer? -> setOfferParent(offer, oldOffer) }
            .map { offer: Offer? -> SetOffer.apply(offer).apply(GetOfferTypes.get()) }
            .map { entity: Offer -> offerRepository.save(entity) }
            .map { offer: Offer -> setOffersUser(offer) }
        return Response("updated.")
    }

    private fun setOfferParent(offer: Offer?, oldOffer: Offer): Offer {
        offer!!.parentOffer = oldOffer
        offer.id = null
        return offer
    }

    fun getOffersByProviderId(id: Long?, offerState: OfferState): List<OfferDTO?> {
        return offerRepository.findByStoreProviderId(id)!!.stream()
            .filter(Predicate { offer: Offer? -> !offer!!.deleted /* todo: wa9ila mchi haka*/ })
            .filter(Predicate { offer: Offer? -> filterOrdersByStatus(offerState, offer!!) })
            .map<OfferDTO?>(Function { offer: Offer? -> offerMapper!!.toDTO(offer) })
            .collect(Collectors.toList())
    }

    private fun filterOrdersByStatus(offerState: OfferState, offer: Offer): Boolean {
        return Optional.ofNullable(offerState)
            .map { state: OfferState -> state == setOfferState(offer) }
            .orElse(true)
    }

    @Named("setOfferState")
    fun setOfferState(offer: Offer): OfferState {
        val date = Date()
        if (date.before(offer.startDate)) {
            return OfferState.PLANNED
        }
        return if (date.after(offer.startDate) && date.before(offer.endDate)) {
            OfferState.ACTIVE
        } else {
            OfferState.EXPIRED
        }
    }

    @Named("getProductList")
    fun getProductList(productVariants: Set<ProductVariant>): List<ProductDTO?> {
        val map: MutableMap<Product?, MutableList<ProductVariant>> = HashMap()
        for (productVariant in productVariants) {
            val product = productVariant.product
            if (map.containsKey(product)) {
                map[product]!!.add(productVariant)
            } else {
                map[product] = ArrayList(listOf(productVariant))
            }
        }
        return map.keys.stream()
            .filter { product: Product? -> !product!!.deleted }
            .peek { key: Product? -> key!!.productVariants = map[key]!! }
            .map { product: Product? -> productMapper!!.toDTO(product) }
            .collect(Collectors.toList())
    }

    fun searchOfferByPosition(lat: Double, lon: Double): List<ProductDTO> {
        val todayDate = Date()
        return offerRepository.findAll().stream()
            .filter(Predicate { offer: Offer? -> !offer!!.deleted /* todo: hna tani wa9ila mchi haka*/ })
            .filter(Predicate { offer: Offer? ->
                todayDate.after(
                    offer!!.startDate
                ) && todayDate.before(offer.endDate)
            })/* todo: hna tani wa9ila mchi haka*/
            .filter(Predicate { offer: Offer? -> isAround(lat, lon, offer!!.store) })
            .map<OfferDTO?>(Function { offer: Offer? -> offerMapper!!.toDTO(offer) })
            .flatMap { offerDto: OfferDTO? -> offerDto!!.products!!.stream() }
            .collect(Collectors.toList())
    }

    private fun isAround(latitude: Double, longitude: Double, store: Store?): Boolean {
        val distance = 12.0
        return distFrom(
            latitude,
            longitude,
            store!!.storeAddress!!.latitude,
            store.storeAddress!!.longitude
        ) < distance
    }

    private fun distFrom(lat1: Double, lng1: Double, lat2: Double?, lng2: Double?): Double {
        val earthRadius = 6371000.0 //meters
        val dLat = Math.toRadians(lat2!! - lat1)
        val dLng = Math.toRadians(lng2!! - lng1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c / 1000
    }

    companion object {
        private val SetOfferFixed = Function { offer: Offer? ->
            offer!!.percentage = 0
            offer
        }
        private val SetOfferPercentage = Function { offer: Offer? ->
            offer!!.newPrice = 0.0
            offer
        }
        private val GetOfferTypes = Supplier<List<Pair<OfferType, Function<Offer?, Offer>>>> {
            val types: List<Pair<OfferType, Function<Offer?, Offer>>> = listOf(
                Pair(OfferType.PERCENTAGE, SetOfferPercentage),
                Pair(OfferType.FIXED, SetOfferFixed)
            )
            types
        }
        private val SetOffer = Function { offer: Offer? ->
            Function { allTypes: List<Pair<OfferType, Function<Offer?, Offer>>> ->
                allTypes.stream()
                    .filter { type: Pair<OfferType, Function<Offer?, Offer>> -> type.key == offer!!.type }
                    .findFirst()
                    .map { obj: Pair<OfferType, Function<Offer?, Offer>> -> obj.value }
                    .map { func: Function<Offer?, Offer> -> func.apply(offer) }
                    .orElseThrow { OfferException("error.offer.type.notFound") }
            }
        }
    }
}