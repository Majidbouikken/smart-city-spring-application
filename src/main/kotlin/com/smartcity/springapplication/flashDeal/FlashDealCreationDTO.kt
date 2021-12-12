package com.smartcity.springapplication.flashDeal

data class FlashDealCreationDTO(
    var id: Long? = null,
    var title: String? = null,
    var content: String? = null,
    var providerId: Long? = null,
)
