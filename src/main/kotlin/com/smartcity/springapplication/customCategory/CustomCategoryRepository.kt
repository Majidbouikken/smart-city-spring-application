package com.smartcity.springapplication.customCategory

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomCategoryRepository : JpaRepository<CustomCategory?, Long?> {
    fun findByStoreProviderId(id: Long?): List<CustomCategory?>?
    fun findByStoreId(id: Long?): List<CustomCategory?>?
}