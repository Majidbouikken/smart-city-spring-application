package com.smartcity.springapplication.storeAddress

import com.smartcity.springapplication.store.Store
import javax.persistence.*

@Entity
data class StoreAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @OneToOne
    var store: Store? = null,
)