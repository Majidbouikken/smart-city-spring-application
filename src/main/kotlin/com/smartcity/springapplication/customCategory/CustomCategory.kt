package com.smartcity.springapplication.customCategory

import com.fasterxml.jackson.annotation.JsonBackReference
import com.smartcity.springapplication.product.Product
import com.smartcity.springapplication.store.Store
import org.hibernate.Hibernate
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded
import javax.persistence.*

@Entity
data class CustomCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val name: String? = null,

    @ManyToOne
    @JsonBackReference
    val store: Store? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "customCategory")
    @IndexedEmbedded
    val products: Set<Product> = HashSet(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CustomCategory

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}