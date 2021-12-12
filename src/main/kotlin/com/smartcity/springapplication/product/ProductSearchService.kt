package com.smartcity.springapplication.product

import com.smartcity.springapplication.category.Category
import com.smartcity.springapplication.user.simpleUser.SearchQuery
import com.smartcity.springapplication.user.simpleUser.SimpleUser
import com.smartcity.springapplication.user.simpleUser.SimpleUserService
import com.smartcity.springapplication.utils.Response
import com.smartcity.springapplication.utils.error_handler.ProductException
import lombok.AllArgsConstructor
import org.hibernate.search.engine.search.common.BooleanOperator
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep
import org.hibernate.search.engine.search.query.SearchResult
import org.hibernate.search.mapper.orm.Search
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Service
@AllArgsConstructor
class ProductSearchService @Autowired constructor(
    val productRepository: ProductRepository,
    val simpleUserService: SimpleUserService,
) {
    @PersistenceContext
    var entityManager: EntityManager? = null
    private val productMapper: ProductMapper? = null
    private val PAGE_SIZE = 10

    /*@Transactional
    fun search(userId: Long?, query: String?, page: Int): List<ProductDTO>? {
        val pageable: Pageable = PageRequest.of(page, PAGE_SIZE)
        val session = Search.session(entityManager)
        val scope = session.scope(Product::class.java)

        //exact tags
        var result: SearchResult<Product> = session.search(scope).where { f ->
            f.simpleQueryString()
                .fields("tags.name")
                .matching(query)
                .defaultOperator(BooleanOperator.AND) as PredicateFinalStep
        }.sort { f -> f.score() }
            .fetch((pageable.pageNumber - 1) * pageable.pageSize, pageable.pageSize) as SearchResult<Product>
        if (result.total().hitCount() == 0L) {

            //exact names
            result = session.search(scope).where { f ->
                f.simpleQueryString()
                    .fields("description", "name")
                    .matching(query)
                    .defaultOperator(BooleanOperator.AND) as PredicateFinalStep
            } // .fuzzy(1)
                //.analyzer("stop")
                //.toPredicate()
                .sort { f -> f.score() }
                .fetch((pageable.pageNumber - 1) * pageable.pageSize, pageable.pageSize) as SearchResult<Product>
            println("exact names")
            if (result.total().hitCount() == 0L) {
                //yel9a w 5lass b tags
                val result1: SearchResult<Product> = session.search(scope).where { f ->
                    f.match()
                        .fields("tags.name")
                        .matching(query)
                        .fuzzy(1, 3)
                        .analyzer("stop")
                }.sort { f -> f.score() }
                    .fetch((pageable.pageNumber - 1) * pageable.pageSize, pageable.pageSize) as SearchResult<Product>

                //yel9a w 5lass b name
                val result2: SearchResult<Product> = session.search(scope).where { f ->
                    f.bool()
                        .filter(
                            f.match()
                                .fields("description", "name")
                                .matching(query) //.analyzer("stop")
                        ) as PredicateFinalStep
                }.sort { f -> f.score() }
                    .fetch((pageable.pageNumber - 1) * pageable.pageSize, pageable.pageSize) as SearchResult<Product>
                if (result1.total().hitCount() > result2.total().hitCount()) {
                    result = result1
                    println("yel9a w 5lass b tags")
                } else {
                    println("yel9a w 5lass b name")
                    result = result2
                }
            }
        }
        val user: SimpleUser = simpleUserService.findById(userId)
        user.searchQueries.add(SearchQuery(user = user, value = query))
        simpleUserService.saveUser(user)
        return PageImpl(
            result.hits()
                .stream()
                .filter { product: Product -> !product.deleted }
                .filter { product: Product? -> isAround(userId, product!!) }
                .map<Any>(productMapper::toDTO)
                .collect(Collectors.toList()),
            pageable,
            pageable.pageSize.toLong()
        ).getContent()
    }

    fun saveClickedProduct(userId: Long?, productId: Long): Response<String> {
        val user = simpleUserService.findById(userId)
        user.clickedProducts.add(
            findProductById(
                productId
            )
        )
        simpleUserService.saveUser(
            user
        )
        return Response("created.")
    }

    private fun findProductById(id: Long): Product = productRepository.findById(id)
        .orElseThrow { ProductException("error.product.notfound") }!!

    private fun isAround(userId: Long?, product: Product): Boolean {
        val distance = 12.0
        val user = simpleUserService.findById(userId)
        val latitude = user.defaultCity!!.latitude
        val longitude = user.defaultCity!!.longitude
        return distFrom(
            latitude,
            longitude,
            product.customCategory!!.store!!.storeAddress!!.latitude,
            product.customCategory!!.store!!.storeAddress!!.longitude
        ) < distance
    }

    private fun distFrom(lat1: Double?, lng1: Double?, lat2: Double?, lng2: Double?): Double {
        val earthRadius = 6371000.0 //meters
        val dLat = Math.toRadians(lat2!! - lat1!!)
        val dLng = Math.toRadians(lng2!! - lng1!!)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c / 1000
    }

    fun findProductAround(userId: Long?, page: Int): List<ProductDTO?> {
        val distance = 12.0
        var pageable: Pageable = PageRequest.of(page, PAGE_SIZE)
        if (page > 0) {
            pageable = PageRequest.of(page - 1, PAGE_SIZE)
        }
        val user = simpleUserService.findById(userId)
        val latitude = user.defaultCity!!.latitude
        val longitude = user.defaultCity!!.longitude
        val productIds = productRepository.findProductAround(latitude!!, longitude!!, distance, pageable)!!
            .content
            .stream()
            .map<Long>(Product::id)
            .collect(Collectors.toList())
        return productRepository.findAllById(productIds).stream()
            .filter(Predicate { product: Product? ->
                getParentCategories(product!!.customCategory!!.store!!.defaultCategories)!!
                    .stream().anyMatch { o: Category -> getParentCategories(user.interestCenter).contains(o) }
            })
            .map<ProductDTO?>(Function { product: Product? -> productMapper!!.toDTO(product) })
            .collect(Collectors.toList())
    }

    private fun getParentCategories(subCategories: Set<Category>): MutableSet<Category?>? = subCategories.stream()
        .map(Category::parentCategory)
        .collect(Collectors.toSet())

    fun findProductAroundMayInterest(userId: Long?, page: Int): List<ProductDTO?> {
        val distance = 12.0
        var pageable: Pageable = PageRequest.of(page, PAGE_SIZE)
        if (page > 0) {
            pageable = PageRequest.of(page - 1, PAGE_SIZE)
        }
        val user = simpleUserService.findById(userId)
        val latitude = user.defaultCity!!.latitude
        val longitude = user.defaultCity!!.longitude
        val productIds = productRepository.findProductAround(latitude!!, longitude!!, distance, pageable)!!
            .content
            .stream()
            .map<Long>(Product::id)
            .collect(Collectors.toList())
        return productRepository.findAllById(productIds).stream()
            .filter(Predicate { product: Product? ->
                product!!.customCategory!!.store!!.defaultCategories.stream()
                    .anyMatch { o: Category? -> user.interestCenter.contains(o) }
            })
            .map<ProductDTO?>(Function { product: Product? -> productMapper!!.toDTO(product) })
            .collect(Collectors.toList())
    }*/
}