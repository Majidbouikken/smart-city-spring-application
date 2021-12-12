package com.smartcity.springapplication.offer

import java.util.Date

data class OfferVariantDTO(
    private val discountCode: String? = null,
    private val type: OfferType? = null,
    private val newPrice: Double? = null,
    private val percentage: Int? = null,
    private val startDate: Date? = null,
    private val endDate: Date? = null,
)