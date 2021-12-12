package com.smartcity.springapplication.attribute.attributeValue

import com.smartcity.springapplication.attribute.Attribute
import com.smartcity.springapplication.productVariantAttributeValue.ProductVariantAttributeValue
import javax.persistence.*

@Entity
data class AttributeValue(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val value: String? = null,

    @OneToMany(mappedBy = "attributeValue", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    var productVariantAttributeValuesAttributeValue: Set<ProductVariantAttributeValue> =
        HashSet(),

    @ManyToOne
    val attribute: Attribute,
)