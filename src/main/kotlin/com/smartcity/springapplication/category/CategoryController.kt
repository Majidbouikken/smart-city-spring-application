package com.smartcity.springapplication.category

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import com.smartcity.springapplication.utils.Results
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@RestController
@RequestMapping("/api/category")
class CategoryController @Autowired constructor(
    val categoryService: CategoryService
) {
    @get:ResponseStatus(HttpStatus.OK)
    @get:GetMapping
    val allCategories: Results<CategoryDTO?>
        get() = Results(categoryService.allCategory)
}