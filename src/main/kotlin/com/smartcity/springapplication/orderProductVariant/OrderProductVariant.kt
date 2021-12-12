package com.smartcity.springapplication.orderProductVariant

import com.smartcity.springapplication.offer.Offer
import com.smartcity.springapplication.order.Order
import com.smartcity.springapplication.productVariant.ProductVariant
import javax.persistence.*

@Entity
class OrderProductVariant(
    @EmbeddedId
    val id: OrderProductVariantId? = null,

    @MapsId("orderId")
    @ManyToOne
    @JoinColumn(name = "order_id")
    val order: Order? = null,

    @MapsId("order_product_variant_id")
    @ManyToOne
    @JoinColumn(name = "product_iariant_id")
    val orderProductVariant: ProductVariant? = null,
    val quantity: Int? = null,

    @OneToOne(cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    @JoinColumn(name = "offer_id")
    var offer: Offer? = null,
)
