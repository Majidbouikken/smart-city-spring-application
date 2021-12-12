package com.smartcity.springapplication.productVariant

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductVariantRepository : JpaRepository<ProductVariant?, Long?>