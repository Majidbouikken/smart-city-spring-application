package com.smartcity.springapplication.user.simpleUser.cart

import java.util.HashSet

data class CartDTO(var cartProductVariants: Set<CartProductVariantDTO> = HashSet())