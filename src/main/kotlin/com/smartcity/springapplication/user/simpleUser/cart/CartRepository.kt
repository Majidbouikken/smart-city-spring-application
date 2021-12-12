package com.smartcity.springapplication.user.simpleUser.cart

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CartRepository : JpaRepository<Cart?, Long?> {
    fun findCartBySimpleUserId(id: Long?): Optional<Cart?>?
}