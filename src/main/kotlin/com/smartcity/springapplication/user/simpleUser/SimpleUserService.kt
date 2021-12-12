package com.smartcity.springapplication.user.simpleUser

import com.smartcity.springapplication.category.Category
import lombok.AllArgsConstructor
import com.smartcity.springapplication.user.UserRegistrationMapper
import com.smartcity.springapplication.category.CategoryService
import com.smartcity.springapplication.category.CategoryMapper
import com.smartcity.springapplication.flashDeal.FlashDealMapper
import com.smartcity.springapplication.offer.OfferMapper
import com.smartcity.springapplication.store.StoreService
import com.smartcity.springapplication.user.simpleUser.city.CityMapper
import com.smartcity.springapplication.nominatim.NominatimService
import com.smartcity.springapplication.utils.error_handler.SimpleUserException
import com.smartcity.springapplication.user.UserDTO
import com.smartcity.springapplication.user.UserRegistrationDTO
import com.smartcity.springapplication.category.CategoryDTO
import com.smartcity.springapplication.utils.error_handler.DateException
import com.smartcity.springapplication.flashDeal.FlashDealDTO
import com.smartcity.springapplication.flashDeal.FlashDeal
import com.smartcity.springapplication.product.ProductDTO
import com.smartcity.springapplication.offer.Offer
import com.smartcity.springapplication.offer.OfferDTO
import com.smartcity.springapplication.user.simpleUser.city.CityDTO
import com.smartcity.springapplication.store.Store
import com.smartcity.springapplication.user.simpleUser.city.City
import com.smartcity.springapplication.utils.DateUtil
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Service
@AllArgsConstructor //@Setter(onMethod=@__({@Autowired}))
class SimpleUserService @Autowired constructor(
    val simpleUserRepository: SimpleUserRepository,
    val categoryService: CategoryService,
    val storeService: StoreService,
    val nominatimService: NominatimService,
) {
    private val userMapper: SimpleUserMapper? = null
    private val userRegestrationMapper: UserRegistrationMapper? = null
    private val categoryMapper: CategoryMapper? = null
    private val simpleUserInformationMapper: SimpleUserInformationMapper? = null
    private val flashDealMapper: FlashDealMapper? = null
    private val offerMapper: OfferMapper? = null
    private val cityMapper: CityMapper? = null

    fun findSimpleUserByEmail(email: String?): Optional<SimpleUser?>? = simpleUserRepository.findByEmail(email)

    fun findSimpleUserByInterestCenterAndAround(store: Store): MutableList<SimpleUser?>? =
        simpleUserRepository.findDistinctByInterestCenterInOrFollowedStoresContaining(
            store.defaultCategories,
            store
        )!!
            .stream()
            .filter(Predicate { user: SimpleUser? -> isAround(user!!, store) })
            .collect(Collectors.toList())

    private fun isAround(user: SimpleUser, store: Store): Boolean {
        val distance = 12.0
        val (id, name, latitude1, longitude1, displayName, country) = user.defaultCity ?: return false
        val latitude = user.defaultCity!!.latitude
        val longitude = user.defaultCity!!.longitude
        return distFrom(
            latitude,
            longitude,
            store.storeAddress!!.latitude,
            store.storeAddress.longitude
        ) < distance
    }

    private fun distFrom(lat1: Double?, lng1: Double?, lat2: Double?, lng2: Double?): Double {
        val earthRadius = 6371000.0 //meters
        val dLat = Math.toRadians(lat2!! - lat1!!)
        val dLng = Math.toRadians(lng2!! - lng1!!)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c / 1000
    }

    private fun isPresentSimpleUserByEmail(email: String?): Boolean =
        simpleUserRepository.findByEmail(email)!!.isPresent

    fun findById(id: Long?): SimpleUser = simpleUserRepository.findById(id!!)
        .orElseThrow { SimpleUserException("error.user.notfound") }!!

    fun saveUser(userDTO: UserDTO): UserRegistrationDTO? {
        return Optional.of(userDTO)
            .map { userDTO: UserDTO? -> userMapper!!.toModel(userDTO) }
            .filter { user: SimpleUser? -> !isPresentSimpleUserByEmail(user!!.email) }
            .map { entity: SimpleUser? -> simpleUserRepository.save(entity!!) }
            .map { simpleUser: SimpleUser? -> userMapper!!.toDTO(simpleUser) }
            .map { userDTO: UserDTO? -> userRegestrationMapper!!.toRegistrationDTO(userDTO) }
            .orElse(null)
    }

    fun saveUser(user: SimpleUser) {
        simpleUserRepository.save(user)
    }

    fun setUserInterestCenter(simpleUserDTO: SimpleUserDTO): Response<String> {
        if (simpleUserDTO.interest != null && simpleUserDTO.interest.isNotEmpty()) {
            val user = findById(simpleUserDTO.id)
            val collect = simpleUserDTO.interest.stream()
                .map { name: String? -> categoryService.findCategoryByName(name) }
                .collect(Collectors.toSet())
            user.interestCenter = collect
            simpleUserRepository.save(user)
        }
        return Response("created.")
    }

    fun getUserInterestCenter(id: Long?): List<CategoryDTO?> {
        val user = findById(id)
        return user.interestCenter
            .stream()
            .map { category: Category? -> categoryMapper!!.toDTO(category) }
            .collect(Collectors.toList())
    }

    fun setUserInformation(simpleUserInformationDTO: SimpleUserInformationDTO): Response<String> {
        if (!DateUtil.isValidDate(simpleUserInformationDTO.birthDay)) {
            throw DateException("error.date.invalid")
        }
        val simpleUser = findById(simpleUserInformationDTO.userId)
        simpleUser.firstName = simpleUserInformationDTO.firstName
        simpleUser.lastName = simpleUserInformationDTO.lastName
        simpleUser.birthDay = DateUtil.parseDate(simpleUserInformationDTO.birthDay)
        simpleUserRepository.save(simpleUser)
        return Response("created.")
    }

    fun getUserInformation(userId: Long): SimpleUserInformationDTO? = Optional.of(findById(userId))
        .map { simpleUser: SimpleUser? -> simpleUserInformationMapper!!.toDTO(simpleUser) }
        .orElseThrow { SimpleUserException("error.user.information") }

    fun getUserFlashDeals(userId: Long?, date: String?): List<FlashDealDTO?> {
        if (!DateUtil.isValidDate(date)) {
            throw DateException("error.date.invalid")
        }
        val startOfDate = LocalDateTime.of(LocalDate.parse(date), LocalTime.MIDNIGHT)
        val endOfDate = LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX)
        return findById(userId).flashDeals.stream()
            .filter { (_, _, _, _, createAt): FlashDeal ->
                createAt!!.isAfter(startOfDate) && createAt.isBefore(
                    endOfDate
                )
            }
            .map { flashDeal: FlashDeal? -> flashDealMapper!!.toDTO(flashDeal) }
            .collect(Collectors.toList())
    }

    fun getUserProductOffers(userId: Long?): List<ProductDTO> {
        val todayDate = Date()
        return findById(userId).offers.stream()
            .filter { (_, _, _, _, _, _, _, _, _, deleted): Offer -> !deleted }
            .filter { (_, _, _, _, _, startDate, endDate): Offer ->
                todayDate.after(startDate) && todayDate.before(
                    endDate
                )
            }
            .map { offer: Offer? -> offerMapper!!.toDTO(offer) }
            .flatMap { offerDTO: OfferDTO? -> offerDTO!!.products!!.stream() }
            .collect(Collectors.toList())
    }

    fun setFlashDeal(simpleUser: SimpleUser, flashDeal: FlashDeal?) {
        simpleUser.flashDeals.add(flashDeal!!)
        simpleUserRepository.save(simpleUser)
    }

    fun setOffers(simpleUser: SimpleUser, offer: Offer?) {
        simpleUser.offers.add(offer!!)
        simpleUserRepository.save(simpleUser)
    }

    fun followStore(storeId: Long?, userId: Long?): Response<String> {
        val user = findById(userId)
        user.followedStores.add(storeService.findStoreById(storeId!!))
        simpleUserRepository.save(user)
        return Response("created.")
    }

    fun stopFollowingStore(storeId: Long?, userId: Long?): Response<String> {
        val user = findById(userId)
        user.followedStores.remove(storeService.findStoreById(storeId!!))
        simpleUserRepository.save(user)
        return Response("deleted.")
    }

    fun isFollowingStore(storeId: Long?, userId: Long?): Response<String> {
        val user = findById(userId)
        return if (user.followedStores.contains(storeService.findStoreById(storeId!!))) {
            Response("isFollowing")
        } else {
            Response("notFollowing")
        }
    }

    fun setUserDefaultCity(cityDto: CityDTO): Response<String> {
        val (name, address) = nominatimService.getCityName(cityDto.latitude, cityDto.longitude)
        cityDto.displayName = name
        cityDto.country = address!!.country
        val user = findById(cityDto.userId)
        user.defaultCity = cityMapper!!.toModel(cityDto)
        simpleUserRepository.save(user)
        return Response("created")
    }

    fun getUserDefaultCity(UserId: Long?): CityDTO? {
        val user = findById(UserId)
        return Optional.of(user.defaultCity!!)
            .map { city: City? -> cityMapper!!.toDTO(city) }
            .orElseThrow { SimpleUserException("error.user.city.notfound") }
    }
}