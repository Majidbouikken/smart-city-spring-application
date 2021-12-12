package com.smartcity.springapplication.policies

import com.smartcity.springapplication.store.StoreService
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [StoreService::class])
interface PoliciesMapper {
    fun toDTO(policies: Policies?): PoliciesDTO?

    @Mapping(source = "policiesDto.providerId", target = "store", qualifiedByName = ["findStoreByProviderId"])
    fun toModel(policiesDto: PoliciesDTO?): Policies?
}