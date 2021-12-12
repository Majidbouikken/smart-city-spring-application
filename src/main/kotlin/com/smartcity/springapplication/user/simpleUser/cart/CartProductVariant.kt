package com.smartcity.springapplication.user.simpleUser.cart

import com.smartcity.springapplication.productVariant.ProductVariant
import javax.persistence.*

@Entity
data class CartProductVariant(
    @EmbeddedId
    val id: CartProductVariantId? = null,

    @MapsId("cartId")
    @ManyToOne
    @JoinColumn(name = "cart_id")
    val cart: Cart? = null,

    @MapsId("cartProductVariantId")
    @ManyToOne
    @JoinColumn(name = "productVariant_id")
    val cartProductVariant: ProductVariant? = null,
    val unit: Int? = null,
)