package com.smartcity.springapplication.category

import com.fasterxml.jackson.annotation.JsonBackReference
import com.smartcity.springapplication.product.Product
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val name: String,

    @OneToMany(mappedBy = "parentCategory", cascade = [CascadeType.ALL])
    var subCategories: Set<Category?> = HashSet(),

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    val parentCategory: Category? = null,

    @ManyToMany(mappedBy = "categories")
    val products: Set<Product> = HashSet()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Category

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}