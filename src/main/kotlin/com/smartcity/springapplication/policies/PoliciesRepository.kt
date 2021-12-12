package com.smartcity.springapplication.policies

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PoliciesRepository : JpaRepository<Policies?, Long?> {
    fun findByStoreId(id: Long?): Optional<Policies?>?
    fun findByStoreProviderId(id: Long?): Optional<Policies?>?
}