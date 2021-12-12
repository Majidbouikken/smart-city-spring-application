package com.smartcity.springapplication.user.simpleUser.cart

import com.smartcity.springapplication.image.ImageDTO
import com.smartcity.springapplication.product.Product
import com.smartcity.springapplication.product.ProductMapper
import com.smartcity.springapplication.productVariant.ProductVariantMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "spring", uses = [ProductMapper::class, ProductVariantMapper::class])
interface CartProductVariantMapper {
    @Mapping(source = "cartProductVariant.cartProductVariant", target = "productVariant")
    @Mapping(
        source = "cartProductVariant.cartProductVariant.product",
        target = "productImage",
        qualifiedByName = ["getMainProductImage"]
    )
    @Mapping(
        source = "cartProductVariant.cartProductVariant.product",
        target = "productName",
        qualifiedByName = ["getMainProductName"]
    )
    @Mapping(
        source = "cartProductVariant.cartProductVariant.product",
        target = "storeName",
        qualifiedByName = ["getStoreName"]
    )
    @Mapping(
        source = "cartProductVariant.cartProductVariant.product",
        target = "storeId",
        qualifiedByName = ["getStoreId"]
    )
    fun toDTO(cartProductVariant: CartProductVariant?): CartProductVariantDTO?

    @Mapping(source = "cartProductVariantDto.productVariant", target = "cartProductVariant")
    fun toModel(cartProductVariantDTO: CartProductVariantDTO?): CartProductVariant?

    @Named("getMainProductImage")
    fun getMainProductImage(product: Product): ImageDTO? = ImageDTO(product.images[0].content)

    @Named("getMainProductName")
    fun getMainProductName(product: Product): String? = product.name

    @Named("getStoreName")
    fun getStoreName(product: Product): String? = product.customCategory?.store?.name

    @Named("getStoreId")
    fun getStoreId(product: Product): Long? = product.customCategory?.store?.id
}