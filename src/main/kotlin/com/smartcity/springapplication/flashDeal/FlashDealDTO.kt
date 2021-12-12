package com.smartcity.springapplication.flashDeal

import java.time.LocalDateTime

data class FlashDealDTO(
    val id: Long? = null,
    val title: String? = null,
    val content: String? = null,
    val createAt: LocalDateTime? = null,
    val storeName: String? = null,
    val storeAddress: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
