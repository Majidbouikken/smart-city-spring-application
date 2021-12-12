package com.smartcity.springapplication.customCategory

import com.smartcity.springapplication.store.StoreService
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [StoreService::class])
interface CustomCategoryMapper {
    @Mapping(source = "customCategory.store.provider.id", target = "provider")
    fun toDTO(customCategory: CustomCategory?): CustomCategoryDTO?

    @Mapping(source = "customCategoryDto.provider", target = "store", qualifiedByName = ["findStoreByProviderId"])
    fun toModel(customCategoryDto: CustomCategoryDTO?): CustomCategory?
}