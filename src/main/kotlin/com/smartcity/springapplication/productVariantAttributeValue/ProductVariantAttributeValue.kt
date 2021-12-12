package com.smartcity.springapplication.productVariantAttributeValue

import com.smartcity.springapplication.attribute.attributeValue.AttributeValue
import com.smartcity.springapplication.productVariant.ProductVariant
import javax.persistence.*

@Entity
data class ProductVariantAttributeValue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "attribute_value_id")
    var attributeValue: AttributeValue? = null,

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    var productVariant: ProductVariant? = null,
)