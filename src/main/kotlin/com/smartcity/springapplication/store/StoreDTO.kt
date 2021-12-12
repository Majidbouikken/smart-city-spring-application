package com.smartcity.springapplication.store

import com.smartcity.springapplication.category.CategoryDTO
import com.smartcity.springapplication.storeAddress.StoreAddressDTO

data class StoreDTO(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val storeAddress: StoreAddressDTO? = null,
    val imageStore: String? = null,
    val defaultCategories: List<CategoryDTO>? = null,
)