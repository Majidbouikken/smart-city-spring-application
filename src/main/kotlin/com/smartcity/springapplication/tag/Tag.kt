package com.smartcity.springapplication.tag

import com.smartcity.springapplication.product.Product
import org.hibernate.search.engine.backend.types.Searchable
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import javax.persistence.*

@Entity
data class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @FullTextField(analyzer = "stop", searchable = Searchable.YES)
    val name: String? = null,

    @ManyToOne
    val product: Product? = null,
)