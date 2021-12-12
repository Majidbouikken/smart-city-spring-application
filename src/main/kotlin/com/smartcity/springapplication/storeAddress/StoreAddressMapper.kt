package com.smartcity.springapplication.storeAddress

import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface StoreAddressMapper {
    fun toDTO(storeAddress: StoreAddress?): StoreAddressDTO?
}