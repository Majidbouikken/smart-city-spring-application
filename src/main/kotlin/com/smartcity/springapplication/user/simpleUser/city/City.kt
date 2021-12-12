package com.smartcity.springapplication.user.simpleUser.city

import javax.persistence.*

@Entity
data class City(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val displayName: String? = null,
    val country: String? = null,
)