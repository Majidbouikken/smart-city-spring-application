package com.smartcity.springapplication.store

import com.smartcity.springapplication.customCategory.CustomCategoryService
import java.io.IOException
import org.springframework.web.multipart.MultipartFile
import com.smartcity.springapplication.utils.Results
import com.smartcity.springapplication.category.CategoryDTO
import com.smartcity.springapplication.customCategory.CustomCategoryDTO
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/store")
class StoreController @Autowired constructor(
    val storeService: StoreService,
    val customCategoryService: CustomCategoryService,
) {
    @PostMapping("/create")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Throws(
        IOException::class
    )
    fun createStore(
        @RequestPart(value = "store") storeCreationDTO: StoreCreationDTO?,
        @RequestPart("image") multipartFile: MultipartFile?
    ): StoreCreationDTO? = storeService.create(storeCreationDTO!!, multipartFile)

    @PostMapping("/category")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun setStoreCategory(
        @RequestParam(value = "id") id: Long?,
        @RequestParam(value = "categories") categories: List<String?>?
    ): Response<String> = storeService.setStoreCategory(id, categories)

    @GetMapping("/category")
    @ResponseStatus(value = HttpStatus.OK)
    fun getStoreCategories(@RequestParam(value = "id") id: Long?): Results<CategoryDTO?> =
        Results(storeService.getStoreCategories(id))

    @PostMapping("/Information")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun setStoreInformation(@RequestBody storeInformationCreationDTO: StoreInformationCreationDTO): Response<String> =
        storeService.setStoreInformation(storeInformationCreationDTO)

    @GetMapping("/Information/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun getStoreInformation(@PathVariable(value = "id") id: Long?): StoreInformationDTO? =
        storeService.getStoreInformation(id)

    @GetMapping("/Information-store/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun getStoreInformationById(@PathVariable(value = "id") id: Long): StoreInformationDTO? =
        storeService.getStoreInformationByStoreId(id)

    @PostMapping("/customCategory/create")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createCustomCategory(@ModelAttribute customCategoryDTO: CustomCategoryDTO?): CustomCategoryDTO? =
        customCategoryService.create(customCategoryDTO!!)

    @DeleteMapping("/customCategory/delete/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    fun deleteCustomCategory(@PathVariable id: Long): Response<String> = customCategoryService.delete(id)

    @PutMapping("/customCategory/update")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateCustomCategory(@ModelAttribute customCategoryDTO: CustomCategoryDTO?): CustomCategoryDTO? =
        customCategoryService.update(customCategoryDTO!!)

    @GetMapping("/customCategory/all/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun getAllCustomCategoryByProvider(@PathVariable id: Long): Results<CustomCategoryDTO?> =
        Results(customCategoryService.getAllByProvider(id))

    @GetMapping("/customCategory/store/all/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun getAllCustomCategoryByStore(@PathVariable id: Long): Results<CustomCategoryDTO?> =
        Results(customCategoryService.getAllByStore(id))

    @GetMapping("/store-around")
    @ResponseStatus(value = HttpStatus.OK)
    fun findStoreByDistance(
        @RequestParam("distance") distance: Double,
        @RequestParam("longitude") longitude: Double,
        @RequestParam("latitude") latitude: Double,
        @RequestParam(value = "category", required = false, defaultValue = "") category: String?
    ): Results<StoreDTO?> = Results(storeService.findStoreByDistance(latitude, longitude, distance, category!!))
}