package com.smartcity.springapplication.customCategory

import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

@Service
class CustomCategoryService @Autowired constructor(
    val customCategoryRepository: CustomCategoryRepository
) {
    val customCategoryMapper: CustomCategoryMapper? = null
    fun create(customCategoryDTO: CustomCategoryDTO): CustomCategoryDTO? {
        customCategoryDTO.id = null
        return Optional.of(customCategoryDTO)
            .map { customCategoryDto: CustomCategoryDTO? -> customCategoryMapper!!.toModel(customCategoryDto) }
            .map { entity: CustomCategory? -> customCategoryRepository.save(entity!!) }
            .map { customCategory: CustomCategory? -> customCategoryMapper!!.toDTO(customCategory) }
            .orElse(null)
    }

    fun findById(id: Long): CustomCategory {
        return customCategoryRepository.findById(id)
            .orElse(null)!!
    }

    fun delete(id: Long): Response<String> {
        customCategoryRepository.findById(id)
            .orElseThrow { CustomCategoryNotFoundException() }
        customCategoryRepository.deleteById(id)
        return Response("deleted.")
    }

    fun update(customCategoryDTO: CustomCategoryDTO): CustomCategoryDTO? {
        return Optional.of(customCategoryDTO)
            .map { customCategoryDto: CustomCategoryDTO? -> customCategoryMapper!!.toModel(customCategoryDto) }
            .map { entity: CustomCategory? -> customCategoryRepository.save(entity!!) }
            .map { customCategory: CustomCategory? -> customCategoryMapper!!.toDTO(customCategory) }
            .orElse(null)
    }

    fun getAllByProvider(id: Long?): List<CustomCategoryDTO?> {
        return customCategoryRepository.findByStoreProviderId(id)!!
            .stream()
            .map<CustomCategoryDTO?>(Function { customCategory: CustomCategory? ->
                customCategoryMapper!!.toDTO(
                    customCategory
                )
            })
            .collect(Collectors.toList())
    }

    fun getAllByStore(id: Long?): List<CustomCategoryDTO?> {
        return customCategoryRepository.findByStoreId(id)!!
            .stream()
            .map<CustomCategoryDTO?>(Function { customCategory: CustomCategory? ->
                customCategoryMapper!!.toDTO(
                    customCategory
                )
            })
            .collect(Collectors.toList())
    }
}