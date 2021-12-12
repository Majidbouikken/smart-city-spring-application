package com.smartcity.springapplication.attribute

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AttributeService @Autowired constructor(
    val attributeRepository: AttributeRepository
) {
    /*private val attributeMapper: AttributeMapper? = null

    fun create(attributeDTO: AttributeDTO?): AttributeDTO {
        return Optional.of(attributeDTO)
            .map(attributeMapper::ToModel)
            .map { entity: S -> attributeRepository!!.save(entity) }
            .map(attributeMapper::toDTO)
            .orElse(null)
    }*/
}