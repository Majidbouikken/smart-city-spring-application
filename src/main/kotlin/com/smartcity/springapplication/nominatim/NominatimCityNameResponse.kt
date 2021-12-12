package com.smartcity.springapplication.nominatim

data class NominatimCityNameResponse(
    val name: String? = null,
    val address: AddressResponse? = null,
)