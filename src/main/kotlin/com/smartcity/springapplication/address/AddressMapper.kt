package com.smartcity.springapplication.address

import com.smartcity.springapplication.user.simpleUser.SimpleUserService
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [SimpleUserService::class])
interface AddressMapper {
    @Mapping(source = "address.user.id", target = "userId")
    fun toDTO(address: Address?): AddressDTO?

    @Mapping(source = "addressDto.userId", target = "user")
    fun toModel(addressDto: AddressDTO?): Address?
}