package com.smartcity.springapplication.store

import com.smartcity.springapplication.category.CategoryDTO

data class StoreInformationDTO(
    val providerId: Long? = null,
    val address: String? = null,
    val telephoneNumber: String? = null,
    val defaultTelephoneNumber: String? = null,
    val defaultCategoriesList: List<CategoryDTO>? = null,
)