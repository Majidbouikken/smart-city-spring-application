package com.smartcity.springapplication.offer

import com.smartcity.springapplication.product.ProductDTO
import java.util.*

data class OfferDTO(
    val id: Long? = null,
    val discountCode: String? = null,
    val type: OfferType? = null,
    val newPrice: Double? = null,
    val percentage: Int? = null,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val products: List<ProductDTO>? = null,
    val offerState: OfferState? = null,
)