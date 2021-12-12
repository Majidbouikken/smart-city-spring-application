package com.smartcity.springapplication.store

import com.smartcity.springapplication.category.CategoryService
import com.smartcity.springapplication.user.provider.ProviderService
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [ProviderService::class, CategoryService::class])
interface StoreInformationMapper {
    @Mapping(
        source = "store.defaultCategories",
        target = "defaultCategoriesList",
        qualifiedByName = ["getCategoriesList"]
    )
    fun toDTO(store: Store?): StoreInformationDTO?
}