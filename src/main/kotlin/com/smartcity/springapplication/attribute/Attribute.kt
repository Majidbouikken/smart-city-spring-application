package com.smartcity.springapplication.attribute

import com.smartcity.springapplication.attribute.attributeValue.AttributeValue
import javax.persistence.*

@Entity
data class Attribute(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val name: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "attribute")
    val attributeValues: Set<AttributeValue> = HashSet(),
)