package com.smartcity.springapplication.user.role

import com.smartcity.springapplication.user.authority.Authority
import com.smartcity.springapplication.user.simpleUser.SimpleUser
import javax.persistence.*

@Entity
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int? = null,

    @Enumerated(EnumType.STRING)
    val name: RoleContext? = null,

    @ManyToMany(mappedBy = "roles")
    val simpleUsers: Set<SimpleUser> = HashSet(),

    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_authority",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id", referencedColumnName = "id")]
    )
    val authorities: Set<Authority> = HashSet(),
)