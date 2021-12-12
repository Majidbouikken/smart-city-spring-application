package com.smartcity.springapplication.user.simpleUser.city

data class CityDTO(
    val id: Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val name: String? = null,
    var displayName: String? = null,
    var country: String? = null,
    val userId: Long? = null,
)