package com.smartcity.springapplication.storeAddress

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreAddressRepository : JpaRepository<StoreAddress?, Long?>