package com.smartcity.springapplication.attribute

import com.smartcity.springapplication.attribute.attributeValue.AttributeValueDTO
import java.util.HashSet

data class AttributeDTO(
    val name: String? = null,
    val attributeValues: Set<AttributeValueDTO> = HashSet()
)