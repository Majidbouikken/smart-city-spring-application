package com.smartcity.springapplication.store

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StoreRepository : JpaRepository<Store?, Long?> {
    fun findByProviderId(id: Long?): Optional<Store?>?

    @Query(
        value = """select * from
(SELECT *,
ST_Distance(ST_Transform(ST_SetSRID(ST_Point(:latitude, :longitude ),4326),2100) , ST_Transform(ST_SetSRID(ST_Point(store_address.latitude, store_address.longitude),4326),2100))/1000 as distance 
FROM store,store_address 
where store.id = store_address.store ) as p
where p.distance < :distance ORDER BY distance ASC
""", nativeQuery = true
    )
    fun findStoreAround(
        @Param("latitude") latitude: Double,
        @Param("longitude") longitude: Double,
        @Param("distance") distance: Double
    ): List<Store?>?
}