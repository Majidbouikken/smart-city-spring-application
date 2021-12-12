package com.smartcity.springapplication.store

data class StoreInformationCreationDTO(
    val providerId: Long? = null,
    val address: String? = null,
    val telephoneNumber: String? = null,
    val defaultTelephoneNumber: String? = null,
    val defaultCategories: List<String>? = null,
)