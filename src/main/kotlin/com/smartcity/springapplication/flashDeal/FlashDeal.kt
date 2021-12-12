package com.smartcity.springapplication.flashDeal

import com.smartcity.springapplication.store.Store
import org.hibernate.Hibernate
import java.time.LocalDateTime
import javax.persistence.*

// par exemple pour un restauranier, il lance un flashdeal "pour les 100 premiers clients, ils auront une reduction"
// on peut penser a creer des flashdeals online, mais pour le moment c'est des flasdeals dans le magasin
@Entity
data class FlashDeal(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val title: String? = null,
    val content: String? = null,

    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: Store? = null,
    var createAt: LocalDateTime? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as FlashDeal

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
