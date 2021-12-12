package com.smartcity.springapplication.policies.taxRange

import com.smartcity.springapplication.policies.Policies
import javax.persistence.*

@Entity
data class TaxRange(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val startRange: Double? = null,
    val endRange: Double? = null,
    val fixAmount: Int? = null,

    @ManyToOne
    @JoinColumn(name = "policies_id")
    var policies: Policies? = null,
)