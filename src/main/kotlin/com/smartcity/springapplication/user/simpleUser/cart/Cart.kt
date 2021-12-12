package com.smartcity.springapplication.user.simpleUser.cart

import com.smartcity.springapplication.user.simpleUser.SimpleUser
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
data class Cart(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @OneToOne
    val simpleUser: SimpleUser? = null,

    @OneToMany(mappedBy = "cart", cascade = [CascadeType.MERGE, CascadeType.REMOVE])
    var cartProductVariants: MutableSet<CartProductVariant> = HashSet<CartProductVariant>(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Cart

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}