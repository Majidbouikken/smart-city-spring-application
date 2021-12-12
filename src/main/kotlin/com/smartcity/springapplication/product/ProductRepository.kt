package com.smartcity.springapplication.product

import com.smartcity.springapplication.category.Category
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product?, Long?> {
    fun findByCategoriesIn(category: Set<Category?>?): List<Product?>?
    fun findAllByCustomCategoryId(id: Long?): List<Product?>?
    fun findAllByCustomCategoryStoreProviderId(id: Long?): List<Product?>?
    fun findAllByCustomCategoryStoreId(id: Long?): List<Product?>?

    @Query(
        value = """SELECT *
FROM
  (SELECT custom_category.id AS custom_category_id
   FROM
     (SELECT *
      FROM
        (SELECT *,
                ST_Distance(ST_Transform(ST_SetSRID(ST_Point(:latitude, :longitude), 4326), 2100), ST_Transform(ST_SetSRID(ST_Point(store_address.latitude, store_address.longitude), 4326), 2100))/1000 AS distance
         FROM store,
              store_address
         WHERE store.id = store_address.store ) AS stores_distance
      WHERE stores_distance.distance < :distance
      ORDER BY distance ASC) AS stores_match_distance,
        custom_category
   WHERE stores_match_distance.store = custom_category.store) AS custom_category_match,
     product
WHERE product.custom_category = custom_category_match.custom_category_id
  AND product.deleted = FALSE""", countQuery = """SELECT count(*)
FROM
  (SELECT custom_category.id AS custom_category_id
   FROM
     (SELECT *
      FROM
        (SELECT *,
                ST_Distance(ST_Transform(ST_SetSRID(ST_Point(:latitude, :longitude), 4326), 2100), ST_Transform(ST_SetSRID(ST_Point(store_address.latitude, store_address.longitude), 4326), 2100))/1000 AS distance
         FROM store,
              store_address
         WHERE store.id = store_address.store ) AS stores_distance
      WHERE stores_distance.distance < :distance
      ORDER BY distance ASC) AS stores_match_distance,
        custom_category
   WHERE stores_match_distance.store = custom_category.store) AS custom_category_match,
     product
WHERE product.custom_category = custom_category_match.custom_category_id
  AND product.deleted = FALSE""", nativeQuery = true
    )
    fun findProductAround(
        @Param("latitude") latitude: Double,
        @Param("longitude") longitude: Double,
        @Param("distance") distance: Double,
        pageable: Pageable?
    ): Page<Product?>?
}