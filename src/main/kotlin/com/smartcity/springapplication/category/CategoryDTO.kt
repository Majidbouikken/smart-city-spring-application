package com.smartcity.springapplication.category

data class CategoryDTO(
    val id: Long? = null,
    val name: String? = null,
    val subCategories: Set<String>? = null,
)
