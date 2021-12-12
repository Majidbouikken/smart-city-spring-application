package com.smartcity.springapplication.order

data class OrderStateDTO(
    var newOrder: Boolean = false,
    var accepted: Boolean = false,
    var rejected: Boolean = false,
    var ready: Boolean = false,
    var delivered: Boolean = false,
    var pickedUp: Boolean = false,
    var received: Boolean = false,
)