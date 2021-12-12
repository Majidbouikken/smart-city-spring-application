package com.smartcity.springapplication.productVariant

import com.smartcity.springapplication.offer.Offer
import com.smartcity.springapplication.orderProductVariant.OrderProductVariant
import com.smartcity.springapplication.product.Product
import com.smartcity.springapplication.productVariantAttributeValue.ProductVariantAttributeValue
import com.smartcity.springapplication.user.simpleUser.cart.CartProductVariant
import javax.persistence.*

@Entity
data class ProductVariant(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @ManyToOne
    var product: Product? = null,

    @OneToMany(mappedBy = "productVariant", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    var productVariantAttributeValuesProductVariant: List<ProductVariantAttributeValue> =
        ArrayList(),

    @OneToMany(mappedBy = "cartProductVariant", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    var cartProductVariants: Set<CartProductVariant> = HashSet(),

    @OneToMany(mappedBy = "orderProductVariant", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    var orderProductVariants: Set<OrderProductVariant> = HashSet(),

    @ManyToMany(mappedBy = "productVariants")
    val offers: Set<Offer> = HashSet(),
    val price: Double? = null,
    val unit: Int? = null,
    val image: String? = null,
)