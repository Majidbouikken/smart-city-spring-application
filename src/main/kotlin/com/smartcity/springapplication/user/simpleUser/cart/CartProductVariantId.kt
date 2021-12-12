package com.smartcity.springapplication.user.simpleUser.cart

import lombok.Builder
import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
@Builder
data class CartProductVariantId(
    var cartId: Long,
    var cartProductVariantId: Long,
) : Serializable