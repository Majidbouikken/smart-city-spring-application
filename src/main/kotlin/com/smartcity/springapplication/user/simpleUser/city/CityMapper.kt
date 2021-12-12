package com.smartcity.springapplication.user.simpleUser.city

import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CityMapper {
    fun toDTO(city: City?): CityDTO?
    fun toModel(cityDto: CityDTO?): City?
}