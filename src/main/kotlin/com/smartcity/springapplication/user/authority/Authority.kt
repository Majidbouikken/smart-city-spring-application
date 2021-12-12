package com.smartcity.springapplication.user.authority

import com.smartcity.springapplication.user.role.Role
import javax.persistence.*

@Entity
data class Authority(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int? = null,
    val permission: String? = null,

    @ManyToMany(mappedBy = "authorities")
    val roles: Set<Role>? = null,
)