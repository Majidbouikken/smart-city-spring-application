package com.smartcity.springapplication.offer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OfferRepository : JpaRepository<Offer?, Long?> {
    fun findByStoreProviderId(id: Long?): List<Offer?>?
}