package com.smartcity.springapplication.category

import com.smartcity.springapplication.utils.error_handler.CategoryException
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.function.Supplier
import java.util.stream.Collectors

@Service
class CategoryService @Autowired constructor(
    val categoryRepository: CategoryRepository
) {
    private val categoryMapper: CategoryMapper? = null

    val allCategory: List<CategoryDTO?>
        get() = categoryRepository.findAll()
            .stream()
            .filter { category -> category!!.parentCategory == null }
            .map(categoryMapper!!::toDTO)
            .collect(Collectors.toList())

    fun findCategoryByName(name: String?): Category {
        return categoryRepository.findByName(name)!!
            .orElseThrow(Supplier { CategoryException("error.category.notFound") })!!
    }

    @Named("getCategoriesList")
    fun getCategoriesList(categories: Set<Category>): List<CategoryDTO>? {
        val map: MutableMap<Category, MutableList<Category?>> = HashMap()
        for (category in categories) {
            val parent: Category = category.parentCategory!!
            if (map.containsKey(parent)) {
                map[parent]!!.add(category)
            } else {
                map[parent] = mutableListOf<Category?>(category)
            }
        }
        return map.keys.stream()
            .peek { key: Category? -> key!!.subCategories = map[key]!!.toSet() }
            .map<CategoryDTO>(categoryMapper!!::toDTO)
            .collect(Collectors.toList())
    }
}