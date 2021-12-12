package com.smartcity.springapplication.user.simpleUser

import com.smartcity.springapplication.user.UserDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface SimpleUserMapper {
    fun toDTO(simpleUser: SimpleUser?): UserDTO?
    fun toModel(userDto: UserDTO?): SimpleUser?
}