package com.smartcity.springapplication.productVariantAttributeValue

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductVariantAttributeValueRepository : JpaRepository<ProductVariantAttributeValue?, Long?>