package com.smartcity.springapplication.user.provider

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProviderRepository : JpaRepository<Provider?, Long?> {
    fun findByEmail(email: String?): Optional<Provider?>?
}