package com.smartcity.springapplication.store

import com.smartcity.springapplication.storeAddress.StoreAddressDTO

data class StoreCreationDTO(
    val name: String? = null,
    val description: String? = null,
    val storeAddress: StoreAddressDTO? = null,
    val provider: Long? = null,
    val imageStore: String? = null,
    val telephoneNumber: String? = null,
    val defaultTelephoneNumber: String? = null,
)
