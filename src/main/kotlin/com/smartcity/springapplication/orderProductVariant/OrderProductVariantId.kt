package com.smartcity.springapplication.orderProductVariant

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class OrderProductVariantId(
    private val orderId: Long? = null,
    private val orderProductVariantId: Long? = null,
) : Serializable