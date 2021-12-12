package com.smartcity.springapplication.address

data class AddressDTO(
    val id: Long? = null,
    val streetNumber: String? = null,
    val admin: String? = null,
    val subAdmin: String? = null,
    val locality: String? = null,
    val streetName: String? = null,
    val postalCode: String? = null,
    val countryCode: String? = null,
    val countryName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val fullAddress: String? = null,
    val apartmentNumber: String? = null,
    val businessName: String? = null,
    val doorCodeName: String? = null,
    val userId: Long? = null,
)