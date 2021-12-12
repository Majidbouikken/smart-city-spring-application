package com.smartcity.springapplication.productVariant

import lombok.AllArgsConstructor
import com.smartcity.springapplication.offer.OfferMapper
import com.smartcity.springapplication.store.exception.MultipleStoreException
import com.smartcity.springapplication.offer.Offer
import com.smartcity.springapplication.offer.OfferVariantDTO
import org.mapstruct.Named
import org.springframework.stereotype.Service
import java.util.*

@Service
@AllArgsConstructor
class ProductVariantService {
    private val productVariantRepository: ProductVariantRepository? = null
    private val offerMapper: OfferMapper? = null
    fun findById(id: Long): ProductVariant {
        return productVariantRepository!!.findById(id)
            .orElseThrow { MultipleStoreException() }!!
    }

    fun getVariantOffer(offers: Set<Offer?>?): Offer? {
        val todayDate = Date()
        return offers?.stream()?.filter { offer: Offer? -> !offer!!.deleted }?.filter { offer: Offer? ->
            todayDate.after(offer!!.startDate) && todayDate.before(
                offer.endDate
            )
        }?.min(Comparator.comparing({ it!!.startDate }, Comparator.naturalOrder()))
            ?.orElse(null)
    }

    @Named("getVariantOffer")
    fun getVariantOfferDto(offers: Set<Offer>?): OfferVariantDTO? {
        val todayDate = Date()
        return offers?.stream()?.filter { (_, _, _, _, _, _, _, _, _, deleted): Offer -> !deleted }
            ?.filter { (_, _, _, _, _, startDate, endDate): Offer ->
                todayDate.after(startDate) && todayDate.before(
                    endDate
                )
            }?.min(Comparator.comparing(Offer::startDate, Comparator.naturalOrder()))
            ?.map { offer: Offer? -> offerMapper!!.toDTOVariant(offer) }?.orElse(null)
    }
}