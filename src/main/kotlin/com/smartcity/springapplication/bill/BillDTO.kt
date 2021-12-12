package com.smartcity.springapplication.bill

data class BillDTO(
    val total: Double? = null,
    val alreadyPaid: Double? = null,
    val createdAt: String? = null,
)