package com.smartcity.springapplication.user.simpleUser.cart

import com.smartcity.springapplication.image.ImageDTO
import com.smartcity.springapplication.productVariant.ProductVariantDTO

data class CartProductVariantDTO(
    val id: CartProductVariantId? = null,
    val productVariant: ProductVariantDTO? = null,
    val unit: Int? = null,
    val productImage: ImageDTO? = null,
    val productName: String? = null,
    val storeName: String? = null,
    val storeId: Long? = null,
)