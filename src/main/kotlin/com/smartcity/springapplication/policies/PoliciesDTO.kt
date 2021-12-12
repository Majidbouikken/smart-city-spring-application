package com.smartcity.springapplication.policies

import com.smartcity.springapplication.policies.taxRange.TaxRangeDTO
import java.util.HashSet

data class PoliciesDTO(
    val id: Long? = null,
    val delivery: Boolean? = null,
    val selfPickUpOption: SelfPickUpOptions? = null,
    val validDuration: Long? = null,
    val tax: Int? = null,
    val taxRanges: Set<TaxRangeDTO> = HashSet(),
    val providerId: Long? = null,
)