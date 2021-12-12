package com.smartcity.springapplication.user.simpleUser

import com.smartcity.springapplication.category.Category
import com.smartcity.springapplication.store.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SimpleUserRepository : JpaRepository<SimpleUser?, Long?> {
    fun findByEmail(email: String?): Optional<SimpleUser?>?
    fun findDistinctByInterestCenterInOrFollowedStoresContaining(
        categories: Set<Category?>?,
        store: Store?
    ): List<SimpleUser?>?
}