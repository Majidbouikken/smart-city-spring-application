package com.smartcity.springapplication.address

import com.smartcity.springapplication.user.simpleUser.SimpleUser
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "address")
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id", unique = true)
    val id: Long? = null,
    val streetNumber: String? = null,
    val admin: String? = null,
    val subAdmin: String? = null,
    val locality: String? = null,
    val streetName: String? = null,
    val postalCode: String? = null,
    val countryCode: String? = null,
    val countryName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val fullAddress: String? = null,
    val apartmentNumber: String? = null,
    val businessName: String? = null,
    val doorCodeName: String? = null,

    @ManyToOne
    @JoinColumn(name = "simple_user_id")
    val user: SimpleUser? = null,
    var deleted: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Address

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}