package com.smartcity.springapplication.productVariant

import com.smartcity.springapplication.offer.OfferVariantDTO
import com.smartcity.springapplication.productVariantAttributeValue.ProductVariantAttributeValueDTO

data class ProductVariantDTO(
    val id: Long? = null,
    var productVariantAttributeValuesProductVariant: List<ProductVariantAttributeValueDTO> = ArrayList(),
    val price: Double? = null,
    val unit: Int? = null,
    val image: String? = null,
    val offer: OfferVariantDTO? = null,
)
