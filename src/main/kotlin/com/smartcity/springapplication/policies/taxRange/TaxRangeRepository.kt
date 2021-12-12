package com.smartcity.springapplication.policies.taxRange

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaxRangeRepository : JpaRepository<TaxRange?, Long?>