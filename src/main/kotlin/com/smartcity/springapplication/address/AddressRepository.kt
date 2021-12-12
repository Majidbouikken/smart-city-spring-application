package com.smartcity.springapplication.address

import com.smartcity.springapplication.address.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository : JpaRepository<Address?, Long?> {
    fun findByUserId(id: Long?): List<Address?>?
}