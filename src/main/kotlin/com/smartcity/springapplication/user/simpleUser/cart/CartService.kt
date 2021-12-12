package com.smartcity.springapplication.user.simpleUser.cart

import lombok.AllArgsConstructor
import com.smartcity.springapplication.user.simpleUser.SimpleUserService
import com.smartcity.springapplication.productVariant.ProductVariantService
import com.smartcity.springapplication.user.simpleUser.SimpleUser
import com.smartcity.springapplication.utils.error_handler.CartException
import com.smartcity.springapplication.productVariant.ProductVariant
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Supplier
import javax.transaction.Transactional

@Service
@AllArgsConstructor
class CartService @Autowired constructor(
    val cartRepository: CartRepository,
    val cartProductVariantRepository: CartProductVariantRepository,
    val simpleUserService: SimpleUserService,
    val productVariantService: ProductVariantService,
) {

    private val cartMapper: CartMapper? = null

    @Transactional
    fun addProductCart(userId: Long, variantId: Long, quantity: Int): Response<String> {
        Optional.of(userId)
            .map { id: Long? -> simpleUserService.findById(id) }
            .map { user: SimpleUser -> setUserCart(user) }
            .map { cart: Cart? -> createCart(cart, variantId, quantity) }
            .map { entity: Cart -> cartRepository.save(entity) }
        return Response("created.")
    }

    @Transactional
    fun deleteProductCart(userId: Long, variantId: Long): Response<String> {
        val cartProductVariant = Optional.of(userId)
            .map { id: Long? -> simpleUserService.findById(id) }
            .map { user: SimpleUser -> getCartId(user) }
            .map { cartId: Long -> getCartProductVariant(cartId, variantId) }
            .get()
        cartProductVariantRepository.delete(cartProductVariant)
        return Response("deleted.")
    }

    fun getUserCart(userId: Long): CartDTO? {
        return Optional.of(userId)
            .map { id: Long? -> simpleUserService.findById(id) }
            .map(SimpleUser::cart)
            .map { cart: Cart? -> cartMapper!!.toDTO(cart) }
            .orElse(
                cartMapper!!.toDTO(
                    setUserCart(simpleUserService.findById(userId))
                )
            )
    }

    private fun getCartProductVariant(cartId: Long, variantId: Long): CartProductVariant {
        return cartProductVariantRepository.findById(
            CartProductVariantId(
                cartId,
                variantId
            )
        ).orElseThrow { CartException("error.cartProductVariant.notfound") }!!
    }

    private fun getCartId(user: SimpleUser): Long {
        return Optional.of(user)
            .filter { user1: SimpleUser -> user1.cart != null }
            .map { user1: SimpleUser -> user1.cart!!.id }
            .get()
    }

    private fun setUserCart(user: SimpleUser): Cart? {
        return Optional.of(user)
            .filter { user1: SimpleUser -> user1.cart == null }
            .map { user: SimpleUser -> initCart(user) }
            .map { entity: Cart -> cartRepository.save(entity) }
            .orElse(user.cart)
    }

    private fun initCart(user: SimpleUser): Cart {
        return Cart(null, user, HashSet())
    }

    private fun createCart(cart: Cart?, variantId: Long, quantity: Int): Cart {
        val cartProductVariant = CartProductVariant(
            CartProductVariantId(cart!!.id!!, variantId),
            cart,
            checkQuantity(productVariantService.findById(variantId), quantity),
            quantity
        )
        cart.cartProductVariants.add(cartProductVariantRepository.save(cartProductVariant))
        return cart
    }

    private fun checkQuantity(productVariant: ProductVariant, quantity: Int): ProductVariant {
        return Optional.of(productVariant)
            .filter { (_, _, _, _, _, _, _, unit): ProductVariant -> quantity <= unit!! }
            .orElseThrow { CartException("error.cart.quantity not available") }
    }

    fun findCartByUserId(id: Long?): Cart {
        return cartRepository.findCartBySimpleUserId(id)!!
            .orElseThrow(Supplier { CartException("error.cart.notfound") })!!
    }

    @Transactional
    fun deleteCart(cart: Cart) {
        cartRepository.delete(cart)
    }

    @Transactional
    fun deleteCartProductVariant(cartProductVariant: CartProductVariant): Boolean {
        cartProductVariantRepository.delete(cartProductVariant)
        return true
    }

    fun findCartProductVariantById(cartProductVariantId: CartProductVariantId): CartProductVariant {
        return cartProductVariantRepository.findById(cartProductVariantId)
            .orElseThrow { CartException("error.cart.product not available") }!!
    }
}