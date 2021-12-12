package com.smartcity.springapplication.orderProductVariant

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderProductVariantRepository : JpaRepository<OrderProductVariant?, OrderProductVariantId?>