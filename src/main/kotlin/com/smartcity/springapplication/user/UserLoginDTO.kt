package com.smartcity.springapplication.user

data class UserLoginDTO(
    val id: Long? = null,
    val email: String? = null,
    val response: String? = null,
    val token: String? = null,
)