package com.smartcity.springapplication.store

import com.smartcity.springapplication.category.Category
import com.smartcity.springapplication.customCategory.CustomCategory
import com.smartcity.springapplication.flashDeal.FlashDeal
import com.smartcity.springapplication.flashDeal.PeriodicityFlash
import com.smartcity.springapplication.offer.Offer
import com.smartcity.springapplication.order.Order
import com.smartcity.springapplication.policies.Policies
import com.smartcity.springapplication.storeAddress.StoreAddress
import com.smartcity.springapplication.user.provider.Provider
import com.smartcity.springapplication.user.simpleUser.SimpleUser
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded
import java.time.LocalDate
import javax.persistence.*

@Entity
data class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val name: String? = null,

    @Lob
    val description: String? = null,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.REMOVE], mappedBy = "store")
    val storeAddress: StoreAddress? = null,
    var telephoneNumber: String? = null,
    var defaultTelephoneNumber: String? = null,
    val imageStore: String? = null,

    @OneToOne
    val provider: Provider? = null,

    @ManyToMany
    @JoinTable(
        name = "store_category",
        joinColumns = [JoinColumn(name = "store_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var defaultCategories: Set<Category> = HashSet(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "store")
    @IndexedEmbedded
    val customCategories: Set<CustomCategory> = HashSet(),

    @OneToMany(mappedBy = "store", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    val flashDeals: Set<FlashDeal> = HashSet(),

    @OneToMany(mappedBy = "store", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    val orders: Set<Order> = HashSet(),

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "store")
    val policies: Policies? = null,

    @OneToMany(mappedBy = "store", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    val offers: Set<Offer> = HashSet(),
    var transmittedFlash: Long? = null,
    var lastFlashStart: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    var periodicityFlash: PeriodicityFlash? = null,

    @ManyToMany(mappedBy = "followedStores")
    val followers: Set<SimpleUser> = HashSet(),
)