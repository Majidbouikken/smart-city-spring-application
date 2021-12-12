package com.smartcity.springapplication.bill

import com.smartcity.springapplication.order.OrderType

data class BillTotalDTO(
    val policyId: Long? = null,
    val total: Double? = null,
    val orderType: OrderType? = null,
)
