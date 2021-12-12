package com.smartcity.springapplication.product

import com.smartcity.springapplication.attribute.AttributeDTO
import com.smartcity.springapplication.image.ImageDTO
import com.smartcity.springapplication.productVariant.ProductVariantDTO
import com.smartcity.springapplication.storeAddress.StoreAddressDTO
import com.smartcity.springapplication.tag.TagDTO
import java.util.ArrayList

class ProductDTO {
    val id: Long? = null
    val tags: Set<TagDTO> = HashSet()
    val productVariants: List<ProductVariantDTO> = ArrayList()
    val description: String? = null
    val name: String? = null
    val images: List<ImageDTO> = ArrayList()
    val customCategory: Long? = null
    val attributes: Set<AttributeDTO> = HashSet()
    val storeAddress: StoreAddressDTO? = null
    val storeName: String? = null
    val storeId: Long? = null
    val storeFollowers: Int? = null
}