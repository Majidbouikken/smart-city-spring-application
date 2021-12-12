package com.smartcity.springapplication.orderProductVariant

import com.smartcity.springapplication.image.ImageDTO
import com.smartcity.springapplication.product.Product
import com.smartcity.springapplication.productVariant.ProductVariantMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "spring", uses = [ProductVariantMapper::class])
interface OrderProductVariantMapper {
    @Mapping(
        source = "orderProductVariant.orderProductVariant",
        target = "productVariant",
        qualifiedByName = ["toDtoOrder"]
    )
    @Mapping(
        source = "orderProductVariant.orderProductVariant.product",
        target = "productImage",
        qualifiedByName = ["getParentProductImage"]
    )
    @Mapping(
        source = "orderProductVariant.orderProductVariant.product",
        target = "productName",
        qualifiedByName = ["getParentProductName"]
    )
    fun toDTO(orderProductVariant: OrderProductVariant?): OrderProductVariantDTO?

    @Named("getParentProductImage")
    fun getParentProductImage(product: Product): ImageDTO? = ImageDTO(product.images[0].content)

    @Named("getParentProductName")
    fun getParentProductName(product: Product): String? = product.name

    @Mapping(source = "orderProductVariantDto.productVariant", target = "orderProductVariant")
    fun toModel(orderProductVariantDto: OrderProductVariantDTO?): OrderProductVariant?
}