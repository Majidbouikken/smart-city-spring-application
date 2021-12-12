package com.smartcity.springapplication.nominatim

import java.io.Serializable

data class NominatimResponse(
    val lat: Double? = null,
    val lon: Double? = null,
    val display_name: String? = null,
    val importance: Double? = null,
) : Serializable