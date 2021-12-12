package com.smartcity.springapplication.order

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
data class OrderState(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    val id: Long? = null,
    var newOrder: Boolean = false,
    var accepted: Boolean = false,
    var rejected: Boolean = false,
    var ready: Boolean = false,
    var delivered: Boolean = false,
    var pickedUp: Boolean = false,
    var received: Boolean = false,
    /*
    private val canceled: Boolean = false;
    private val archived: Boolean = false,
    private val archivedProblem: Boolean = false
    */
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as OrderState

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }

    fun isNewOrder(): Boolean = newOrder
    fun isAccepted(): Boolean = accepted
    fun isRejected(): Boolean = rejected
    fun isReady(): Boolean = ready
    fun isDelivered(): Boolean = delivered
    fun isPickedUp(): Boolean = pickedUp
    fun isReceived(): Boolean = received
}