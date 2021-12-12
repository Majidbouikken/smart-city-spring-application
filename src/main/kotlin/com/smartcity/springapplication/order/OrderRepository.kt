package com.smartcity.springapplication.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import org.springframework.data.domain.Sort

@Repository
interface OrderRepository : JpaRepository<Order?, Long?> {
    fun findByStoreProviderId(id: Long?, sort: Sort?): List<Order?>?
    fun findByUserId(id: Long?, sort: Sort?): List<Order?>?
    fun findByStoreProviderIdAndCreateAtBetween(
        id: Long?,
        start: LocalDateTime?,
        end: LocalDateTime?,
        sort: Sort?
    ): List<Order?>?
}