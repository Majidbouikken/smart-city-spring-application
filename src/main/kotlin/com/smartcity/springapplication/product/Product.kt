package com.smartcity.springapplication.product

import com.smartcity.springapplication.attribute.Attribute
import com.smartcity.springapplication.category.Category
import com.smartcity.springapplication.productVariant.ProductVariant
import com.smartcity.springapplication.customCategory.CustomCategory
import com.smartcity.springapplication.image.Image
import com.smartcity.springapplication.tag.Tag
import org.hibernate.Hibernate
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded
import org.springframework.stereotype.Indexed
import javax.persistence.*

@Indexed
@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Lob
    @FullTextField
    val name: String? = null,

    @Lob
    @FullTextField
    val description: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "product")
    @IndexedEmbedded
    val tags: Set<Tag> = HashSet(),

    @ManyToMany
    @JoinTable(
        name = "product_category",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    val categories: Set<Category> = HashSet(),

    @ManyToOne
    var customCategory: CustomCategory? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "product")
    val images: List<Image> = ArrayList(),

    @OneToMany(cascade = [CascadeType.MERGE, CascadeType.REMOVE], mappedBy = "product")
    var productVariants: List<ProductVariant> = ArrayList(),

    @OneToMany
    @JoinColumn(name = "product")
    val attributes: MutableSet<Attribute> = HashSet(),

    var deleted: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Product

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}