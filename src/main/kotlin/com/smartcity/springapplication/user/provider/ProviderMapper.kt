package com.smartcity.springapplication.user.provider

import com.smartcity.springapplication.user.UserDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ProviderMapper {
    fun toDTO(provider: Provider?): UserDTO?
    fun toModel(userDto: UserDTO?): Provider?
}