package com.smartcity.springapplication.offer

import com.smartcity.springapplication.store.Store
import com.smartcity.springapplication.productVariant.ProductVariant
import com.fasterxml.jackson.annotation.JsonBackReference
import java.util.*
import javax.persistence.*

@Entity
data class Offer(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    val discountCode: String? = null,

    @Enumerated(EnumType.STRING)
    val type: OfferType? = null,
    var newPrice: Double? = null,
    var percentage: Int? = null,
    val startDate: Date? = null,
    val endDate: Date? = null,

    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: Store? = null,

    @ManyToMany
    @JoinTable(
        name = "offer_product_variant",
        joinColumns = [JoinColumn(name = "offer_id")],
        inverseJoinColumns = [JoinColumn(name = "productVariant_id")]
    )
    val productVariants: Set<ProductVariant> = HashSet(),
    var deleted: Boolean = false,

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    var parentOffer: Offer? = null,
)
