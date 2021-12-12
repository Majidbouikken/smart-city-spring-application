package com.smartcity.springapplication.image

import com.smartcity.springapplication.product.Product
import javax.persistence.*

@Entity
data class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val content: String? = null,

    @ManyToOne
    var product: Product? = null,
)