package com.smartcity.springapplication.user.simpleUser.cart

import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController @Autowired constructor(
    val cartService: CartService
) {
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    fun addProductToCart(
        @RequestParam(name = "userId") userId: Long,
        @RequestParam(name = "variantId") variantId: Long,
        @RequestParam(name = "quantity") quantity: Int
    ): Response<String> = cartService.addProductCart(userId, variantId, quantity)

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    fun deleteProductFromCart(
        @RequestParam(name = "userId") userId: Long,
        @RequestParam(name = "variantId") variantId: Long
    ): Response<String> = cartService.deleteProductCart(userId, variantId)

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getUserCart(@PathVariable userId: Long): CartDTO? = cartService.getUserCart(userId)
}