package com.smartcity.springapplication.attribute.attributeValue

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttributeValueRepository : JpaRepository<AttributeValue?, Long?>