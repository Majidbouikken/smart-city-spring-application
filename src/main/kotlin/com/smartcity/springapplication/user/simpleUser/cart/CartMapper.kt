package com.smartcity.springapplication.user.simpleUser.cart

import org.mapstruct.Mapper

@Mapper(componentModel = "spring", uses = [CartProductVariantMapper::class])
interface CartMapper {
    fun toDTO(cart: Cart?): CartDTO?
    fun toModel(cartDto: CartDTO?): Cart?
}