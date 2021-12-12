package com.smartcity.springapplication.user

data class UserDTO(
    val id: Long? = null,
    val email: String? = null,
    val userName: String? = null,
    val passWord: String? = null,
    val passWord2: String? = null,
)