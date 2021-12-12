package com.smartcity.springapplication.flashDeal

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface FlashDealRepository : JpaRepository<FlashDeal?, Long?> {
    fun findByStoreProviderId(id: Long?): List<FlashDeal?>?
    fun findByStoreProviderIdAndCreateAtBetween(
        id: Long,
        start: LocalDateTime?,
        end: LocalDateTime?
    ): List<FlashDeal?>?
}