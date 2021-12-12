package com.smartcity.springapplication.attribute

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttributeRepository : JpaRepository<Attribute?, Long?>