package com.smartcity.springapplication.policies

import com.smartcity.springapplication.policies.taxRange.TaxRange
import com.smartcity.springapplication.store.Store
import javax.persistence.*

@Entity
data class Policies(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    val delivery: Boolean? = null,

    @Enumerated(EnumType.STRING)
    var selfPickUpOption: SelfPickUpOptions? = null,
    val validDuration: Long? = null,
    var tax: Int? = null,

    @OneToMany(mappedBy = "policies", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    var taxRanges: MutableSet<TaxRange> = HashSet(),

    @OneToOne
    val store: Store? = null,
)