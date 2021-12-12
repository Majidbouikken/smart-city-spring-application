package com.smartcity.springapplication.user.simpleUser

import javax.persistence.*

@Entity
data class SearchQuery(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val value: String? = null,

    @ManyToOne
    @JoinColumn(name = "simple_user_id")
    val user: SimpleUser? = null,
)