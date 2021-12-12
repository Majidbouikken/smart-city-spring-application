package com.smartcity.springapplication.offer

data class OfferCreationDTO(
    var id: Long? = null,
    val discountCode: String? = null,
    val type: OfferType? = null,
    val newPrice: Double? = null,
    val percentage: Int? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val providerId: Long? = null,
    val productVariantsId: Set<Long>? = null,
)