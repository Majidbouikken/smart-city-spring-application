package com.smartcity.springapplication.user.simpleUser

data class SimpleUserInformationDTO(
    val userId: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDay: String? = null,
)