package com.smartcity.springapplication.orderProductVariant

import com.smartcity.springapplication.image.ImageDTO
import com.smartcity.springapplication.offer.OfferVariantDTO
import com.smartcity.springapplication.productVariant.ProductVariantDTO

data class OrderProductVariantDTO(
    val id: OrderProductVariantId? = null,
    val productVariant: ProductVariantDTO? = null,
    val quantity: Int? = null,
    val productImage: ImageDTO? = null,
    val productName: String? = null,
    val offer: OfferVariantDTO? = null,
)
