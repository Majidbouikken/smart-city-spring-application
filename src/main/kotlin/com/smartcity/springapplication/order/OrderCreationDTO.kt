package com.smartcity.springapplication.order

import com.smartcity.springapplication.address.AddressDTO
import com.smartcity.springapplication.user.simpleUser.cart.CartProductVariantId

data class OrderCreationDTO(
    var userId: Long? = null,
    var storeId: Long? = null,
    var orderType: OrderType? = null,
    var address: AddressDTO? = null,
    var cartProductVariantIds: List<CartProductVariantId>? = null,
    var receiverFirstName: String? = null,
    var receiverLastName: String? = null,
    var receiverBirthDay: String? = null,
)