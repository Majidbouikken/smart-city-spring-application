package com.smartcity.springapplication.productVariant

import com.smartcity.springapplication.offer.OfferMapper
import com.smartcity.springapplication.product.ProductMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [ProductVariantService::class, OfferMapper::class, ProductMapper::class])
interface ProductVariantMapper {
    fun toModel(productVariantDto: ProductVariantDTO?): ProductVariant?

    @Mapping(source = "productVariant.offers", target = "offer", qualifiedByName = ["getVariantOffer"])
    fun toDTO(productVariant: ProductVariant?): ProductVariantDTO?

    //@Named("toDtoOrder")
    //fun toDTOOrder(productVariant: ProductVariant?): ProductVariantDTO?
}