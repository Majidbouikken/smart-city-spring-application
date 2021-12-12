package com.smartcity.springapplication.user

data class UserRegistrationDTO(
    val id: Long? = null,
    val email: String? = null,
    val userName: String? = null,
    var response: String? = null,
)