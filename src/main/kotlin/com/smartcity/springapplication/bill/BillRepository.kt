package com.smartcity.springapplication.bill

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BillRepository : JpaRepository<Bill?, Long?>