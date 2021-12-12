package com.smartcity.springapplication.user.simpleUser.cart

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartProductVariantRepository : JpaRepository<CartProductVariant?, CartProductVariantId?>