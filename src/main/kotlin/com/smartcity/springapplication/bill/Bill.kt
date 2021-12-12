package com.smartcity.springapplication.bill

import com.smartcity.springapplication.order.Order
import com.smartcity.springapplication.store.Store
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Bill(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val total: Double? = null,
    val alreadyPaid: Double? = null,
    val createdAt: LocalDateTime? = null,

    @OneToOne
    @JoinColumn(name = "order_id")
    val order: Order? = null,

    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: Store? = null,
)