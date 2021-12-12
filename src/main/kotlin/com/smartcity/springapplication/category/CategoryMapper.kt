package com.smartcity.springapplication.category

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import java.util.stream.Collectors

@Mapper(componentModel = "spring")
interface CategoryMapper {
    @Mapping(source = "category.subCategories", target = "subCategories", qualifiedByName = ["subCategory"])
    fun toDTO(category: Category?): CategoryDTO?

    @Named("subCategory")
    fun subCategory(categories: Set<Category>): Set<String>? = categories.stream()
        .map(Category::name)
        .collect(Collectors.toSet())
//  Category toModel(CategoryDto categoryDto);
}