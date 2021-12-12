package com.smartcity.springapplication.user.provider

import com.smartcity.springapplication.store.Store
import com.smartcity.springapplication.user.authority.Authority
import com.smartcity.springapplication.user.role.Role
import com.smartcity.springapplication.user.User
import javax.persistence.*

@Entity
data class Provider(
    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "provider")
    val store: Store? = null,

    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], fetch = FetchType.EAGER)
    @JoinTable(
        name = "provider_role",
        joinColumns = [JoinColumn(name = "Provider_ID", referencedColumnName = "ID")],
        inverseJoinColumns = [JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")]
    )
    val roles: Set<Role> = HashSet(),

    @Transient
    var authorities: MutableSet<Authority?>? = HashSet(),

    val accountNonExpired: Boolean = true,

    val accountNonLocked: Boolean = true,

    val credentialsNonExpired: Boolean = true,

    val enabled: Boolean = true,
) : User()