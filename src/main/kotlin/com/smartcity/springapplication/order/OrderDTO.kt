package com.smartcity.springapplication.order

import com.smartcity.springapplication.address.AddressDTO
import com.smartcity.springapplication.bill.BillDTO
import com.smartcity.springapplication.orderProductVariant.OrderProductVariantDTO
import java.time.LocalDateTime

data class OrderDTO(
    var id: Long? = null,
    var orderProductVariants: Set<OrderProductVariantDTO> = HashSet(),
    var bill: BillDTO? = null,
    var orderType: OrderType? = null,
    var address: AddressDTO? = null,
    var orderState: OrderStateDTO? = null,
    var receiverFirstName: String? = null,
    var receiverLastName: String? = null,
    var receiverBirthDay: String? = null,
    var createAt: LocalDateTime? = null,
    var validDuration: Long? = null,
    var storeName: String? = null,
    var storeAddress: String? = null,
    var providerNote: String? = null,
)