package com.smartcity.springapplication.product

import com.smartcity.springapplication.attribute.Attribute
import com.smartcity.springapplication.attribute.attributeValue.AttributeValue
import com.smartcity.springapplication.attribute.attributeValue.AttributeValueDTO
import com.smartcity.springapplication.customCategory.CustomCategoryService
import com.smartcity.springapplication.productVariant.ProductVariantMapper
import com.smartcity.springapplication.store.StoreService
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named

@Mapper(
    componentModel = "spring",
    uses = [CustomCategoryService::class, ProductVariantMapper::class, StoreService::class]
)
interface ProductMapper {
    @Mapping(source = "product.customCategory.id", target = "customCategory")
    @Mapping(
        source = "product.customCategory.store",
        target = "storeAddress",
        qualifiedByName = ["getStoreAddressUpdated"]
    )
    @Mapping(source = "product.customCategory.store", target = "storeName", qualifiedByName = ["getStoreName"])
    @Mapping(source = "product.customCategory.store", target = "storeId", qualifiedByName = ["getStoreId"])
    @Mapping(
        source = "product.customCategory.store",
        target = "storeFollowers",
        qualifiedByName = ["getStoreFollowers"]
    )
    fun toDTO(product: Product?): ProductDTO?

    @Mapping(source = "productDTO.customCategory", target = "customCategory")
    fun toModel(productDTO: ProductDTO?): Product?

    @Mapping(source = "attributeValue.attribute.name", target = "attribute")
    fun toAttributeDTO(attributeValue: AttributeValue?): AttributeValueDTO?

    @Mappings(
        Mapping(
            source = "attributeValueDto.value",
            target = "value"
        ), Mapping(source = "attributeValueDto.attribute", target = "attribute", qualifiedByName = ["binType2"])
    )
    fun toAttributeModel(attributeValueDto: AttributeValueDTO?): AttributeValue?

    @Named("binType2")
    fun locationToBinType(attribute: String?): Attribute? = Attribute(name = attribute)
}