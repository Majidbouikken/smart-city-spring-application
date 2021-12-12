package com.smartcity.springapplication.offer

import com.smartcity.springapplication.product.ProductMapper
import com.smartcity.springapplication.productVariant.ProductVariantService
import com.smartcity.springapplication.store.StoreService
import com.smartcity.springapplication.utils.DateUtil
import org.mapstruct.Mapper
import org.mapstruct.Mapping
//import org.mapstruct.ap.shaded.freemarker.template.utility.DateUtil

@Mapper(
    componentModel = "spring",
    uses = [StoreService::class, ProductVariantService::class, DateUtil::class, ProductMapper::class, OfferService::class]
)
interface OfferMapper {
    @Mapping(source = "offerCreationDto.providerId", target = "store", qualifiedByName = ["findStoreByProviderId"])
    @Mapping(source = "offerCreationDto.startDate", target = "startDate", qualifiedByName = ["parseDateTime"])
    @Mapping(source = "offerCreationDto.endDate", target = "endDate", qualifiedByName = ["parseDateTime"])
    @Mapping(source = "offerCreationDto.productVariantsId", target = "productVariants")
    fun toModel(offerCreationDto: OfferCreationDTO?): Offer?

    @Mapping(source = "offer.productVariants", target = "products", qualifiedByName = ["getProductList"])
    @Mapping(source = "offer", target = "offerState", qualifiedByName = ["setOfferState"])
    fun toDTO(offer: Offer?): OfferDTO?
    fun toDTOVariant(offer: Offer?): OfferVariantDTO?
}