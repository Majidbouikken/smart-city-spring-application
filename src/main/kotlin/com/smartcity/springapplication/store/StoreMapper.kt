package com.smartcity.springapplication.store

import com.smartcity.springapplication.category.CategoryMapper
import com.smartcity.springapplication.storeAddress.StoreAddressMapper
import com.smartcity.springapplication.user.provider.ProviderService
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [ProviderService::class, StoreAddressMapper::class, CategoryMapper::class])
interface StoreMapper {
    @Mapping(source = "store.provider.id", target = "provider")
    fun toDTOCreation(store: Store?): StoreCreationDTO?
    fun toDTO(store: Store?): StoreDTO?
    fun toModel(storeCreationDto: StoreCreationDTO?): Store?
}