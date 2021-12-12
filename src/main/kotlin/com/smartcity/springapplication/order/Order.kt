@file:Suppress("JpaDataSourceORMInspection")

package com.smartcity.springapplication.order

import com.smartcity.springapplication.address.Address
import com.smartcity.springapplication.bill.Bill
import com.smartcity.springapplication.orderProductVariant.OrderProductVariant
import com.smartcity.springapplication.store.Store
import com.smartcity.springapplication.user.simpleUser.SimpleUser
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "simple_user_id")
    val user: SimpleUser? = null,

    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: Store? = null,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    var orderProductVariants: Set<OrderProductVariant> = HashSet(),

    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "bill_id")
    var bill: Bill? = null,

    @Enumerated(EnumType.STRING)
    var orderType: OrderType? = null,

    @OneToOne(cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    @JoinColumn(name = "address_id")
    val address: Address? = null,

    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "order_state")
    var orderState: OrderState? = null,
    val receiverFirstName: String? = null,
    val receiverLastName: String? = null,
    val receiverBirthDay: String? = null,
    var createAt: LocalDateTime? = null,
    val validDuration: Long? = null,

    @Lob
    var providerNote: String? = null,

    @Lob
    var providerComment: String? = null,
    var providerDate: Date? = null,
)
