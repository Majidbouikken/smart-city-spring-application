package com.smartcity.springapplication.user.simpleUser

import com.smartcity.springapplication.address.Address
import com.smartcity.springapplication.category.Category
import com.smartcity.springapplication.flashDeal.FlashDeal
import com.smartcity.springapplication.offer.Offer
import com.smartcity.springapplication.order.Order
import com.smartcity.springapplication.product.Product
import com.smartcity.springapplication.store.Store
import com.smartcity.springapplication.user.authority.Authority
import com.smartcity.springapplication.user.role.Role
import com.smartcity.springapplication.user.User
import com.smartcity.springapplication.user.simpleUser.cart.Cart
import com.smartcity.springapplication.user.simpleUser.city.City
import java.util.*
import javax.persistence.*

@Entity
class SimpleUser(
    var firstName: String? = null,
    var lastName: String? = null,
    var birthDay: Date? = null,

    @ManyToMany
    @JoinTable(
        name = "simple_users_clicked_product",
        joinColumns = [JoinColumn(name = "users_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    var clickedProducts: MutableSet<Product> = HashSet(),

    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "city_id")
    var defaultCity: City? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.MERGE, CascadeType.REMOVE, CascadeType.PERSIST])
    var searchQueries: MutableSet<SearchQuery> = HashSet(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    val addressSet: Set<Address> = HashSet(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    val orders: Set<Order> = HashSet(),

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "simpleUser")
    val cart: Cart? = null,

    @ManyToMany
    @JoinTable(
        name = "simple_users_category",
        joinColumns = [JoinColumn(name = "users_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var interestCenter: Set<Category> = HashSet(),

    @ManyToMany
    @JoinTable(
        name = "simple_users_flash_deals",
        joinColumns = [JoinColumn(name = "users_id")],
        inverseJoinColumns = [JoinColumn(name = "flashDeals_id")]
    )
    val flashDeals: MutableSet<FlashDeal> = HashSet(),

    @ManyToMany
    @JoinTable(
        name = "simple_users_offers",
        joinColumns = [JoinColumn(name = "users_id")],
        inverseJoinColumns = [JoinColumn(name = "offer_id")]
    )
    val offers: MutableSet<Offer> = HashSet(),

    @ManyToMany
    @JoinTable(
        name = "simple_users_store",
        joinColumns = [JoinColumn(name = "users_id")],
        inverseJoinColumns = [JoinColumn(name = "store_id")]
    )
    val followedStores: MutableSet<Store> = HashSet(),

    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], fetch = FetchType.EAGER)
    @JoinTable(
        name = "simple_user_role",
        joinColumns = [JoinColumn(name = "SimpleUser_ID", referencedColumnName = "ID")],
        inverseJoinColumns = [JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")]
    )
    val roles: Set<Role> = HashSet<Role>(),

    @Transient
    val authorities: Set<Authority> = HashSet(),

    val accountNonExpired: Boolean = true,

    val accountNonLocked: Boolean = true,

    val credentialsNonExpired: Boolean = true,

    val enabled: Boolean = true,
) : User()