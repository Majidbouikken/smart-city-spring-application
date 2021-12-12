package com.smartcity.springapplication.nominatim

data class City(
    val city: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val importance: Double? = null,
)