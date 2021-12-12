package com.smartcity.springapplication.user

import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface UserRegistrationMapper {
    fun toRegistrationDTO(userDto: UserDTO?): UserRegistrationDTO?

    companion object {
        @AfterMapping
        fun update(@MappingTarget userRegistrationDTO: UserRegistrationDTO): UserRegistrationDTO? {
            userRegistrationDTO.response = "successfully registered"
            return userRegistrationDTO
        }
    }
}