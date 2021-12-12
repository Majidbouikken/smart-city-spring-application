package com.smartcity.springapplication.policies.taxRange

data class TaxRangeDTO(
    val startRange: Double? = null,
    val endRange: Double? = null,
    val fixAmount: Int? = null,
)