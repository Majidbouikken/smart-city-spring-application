package com.smartcity.springapplication.product

import com.smartcity.springapplication.attribute.Attribute
import lombok.AllArgsConstructor
import com.smartcity.springapplication.customCategory.CustomCategoryService
import com.smartcity.springapplication.productVariant.ProductVariantRepository
import com.smartcity.springapplication.image.ImageRepository
import com.smartcity.springapplication.attribute.AttributeRepository
import com.smartcity.springapplication.attribute.attributeValue.AttributeValue
import com.smartcity.springapplication.attribute.attributeValue.AttributeValueRepository
import com.smartcity.springapplication.productVariantAttributeValue.ProductVariantAttributeValueRepository
import com.smartcity.springapplication.customCategory.CustomCategory
import com.smartcity.springapplication.customCategory.CustomCategoryNotFoundException
import com.smartcity.springapplication.image.Image
import org.springframework.web.multipart.MultipartFile
import com.smartcity.springapplication.productVariantAttributeValue.ProductVariantAttributeValue
import com.smartcity.springapplication.productVariant.ProductVariant
import com.smartcity.springapplication.storage.FileStorage
import java.io.IOException
import com.smartcity.springapplication.storage.FileStorageException
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier
import java.util.stream.Collectors
import javax.transaction.Transactional

@Service
@AllArgsConstructor
class ProductService @Autowired constructor(
    val customCategoryService: CustomCategoryService,
    val productRepository: ProductRepository,
    val productVariantRepository: ProductVariantRepository,
    val imageRepository: ImageRepository,
    val attributeRepository: AttributeRepository,
    val attributeValueRepository: AttributeValueRepository,
    val productVariantAttributeValueRepository: ProductVariantAttributeValueRepository,
) {
    private val productMapper: ProductMapper? = null
    private val fileStorage: FileStorage? = null

    fun updateProductsCustomCategory(productIds: List<Long>, newCustomCategoryId: Long?): Response<String> {
        val newCategory = customCategoryService.findById(newCustomCategoryId!!)
        val collect = productIds.stream()
            .map { id: Long -> findProductById(id) }
            .map { product: Product -> setNewCustomCategory(product, newCategory) }
            .map { entity: Product -> productRepository.save(entity) }
            .collect(Collectors.toList())
        return Response("updated.")
    }

    private fun setNewCustomCategory(product: Product, customCategory: CustomCategory): Product {
        product.customCategory = customCategory
        return product
    }

    fun findProductById(id: Long): Product {
        return productRepository.findById(id)
            .orElseThrow(Supplier<CustomCategoryNotFoundException> { CustomCategoryNotFoundException() })!!
    }

    fun getProductByCustomCategoryId(id: Long?): List<ProductDTO?> {
        return productRepository.findAllByCustomCategoryId(id)!!
            .stream()
            .filter(Predicate { product: Product? -> !product!!.deleted })
            .map<ProductDTO?>(Function { product: Product? -> productMapper!!.toDTO(product) })
            .collect(Collectors.toList())
    }

    fun getProductByCustomCategoryStoreProviderId(id: Long?): List<ProductDTO?> {
        return productRepository.findAllByCustomCategoryStoreProviderId(id)!!
            .stream()
            .filter(Predicate { product: Product? -> !product!!.deleted })
            .map<ProductDTO?>(Function { product: Product? -> productMapper!!.toDTO(product) })
            .collect(Collectors.toList())
    }

    fun getProductByCustomCategoryStoreId(id: Long?): List<ProductDTO?> {
        return productRepository.findAllByCustomCategoryStoreId(id)!!
            .stream()
            .filter(Predicate { product: Product? -> !product!!.deleted })
            .map<ProductDTO?>(Function { product: Product? -> productMapper!!.toDTO(product) })
            .collect(Collectors.toList())
    }

    @Transactional
    fun deleteProduct(id: Long?): Response<String> {
        val product = productRepository.findById(id!!)
            .orElseThrow(Supplier<CustomCategoryNotFoundException> { CustomCategoryNotFoundException() })!!
        product.deleted = true
        productRepository.save(product)
        return Response("deleted.")
    }

    fun updateProduct(
        productDTO: ProductDTO,
        productImages: List<MultipartFile>?,
        variancesImages: List<MultipartFile>?
    ): ProductDTO? {
        return if (productRepository.findById(productDTO.id!!).isPresent) {
            deleteProduct(productDTO.id)
            productImages?.let { saveProductImages(it) }
            val variancesImagesMap: MutableMap<String?, MultipartFile> = HashMap()
            if (variancesImages != null) {
                for (image in variancesImages) {
                    variancesImagesMap[image.originalFilename] = image
                }
            }
            Optional.of(productDTO)
                .map { productDTO: ProductDTO? -> productMapper!!.toModel(productDTO) }
                .map { product: Product? ->
                    val savedAttribute = product!!.attributes
                        .stream()
                        .map { (_, name): Attribute ->
                            attributeRepository.save(Attribute(null, name, HashSet()))
                        }
                        .collect(Collectors.toSet())
                    val savedAttributeValue: MutableSet<AttributeValue> = HashSet()
                    for ((_, name, attributeValues) in product.attributes) {
                        for ((_, value) in attributeValues) {
                            savedAttributeValue.add(
                                attributeValueRepository.save(
                                    AttributeValue(
                                        null,
                                        value,
                                        HashSet(),
                                        savedAttribute.stream().filter { (_, name1): Attribute -> name1 == name }
                                            .findFirst()
                                            .get()
                                    )))
                        }
                    }
                    val savedProductVariant: MutableList<ProductVariant> = ArrayList()
                    for ((_, _, productVariantAttributeValuesProductVariant, _, _, _, price, unit, image) in product.productVariants) {
                        val productVariantAttributeValue: MutableList<ProductVariantAttributeValue> = ArrayList()
                        for ((_, attributeValue1) in productVariantAttributeValuesProductVariant) {
                            val savedProductVariantAttributeValue: MutableList<ProductVariantAttributeValue> =
                                ArrayList()
                            savedProductVariantAttributeValue.add(
                                productVariantAttributeValueRepository.save(
                                    ProductVariantAttributeValue(
                                        null,
                                        savedAttributeValue
                                            .stream()
                                            .filter { (_, value): AttributeValue -> value == attributeValue1!!.value }
                                            .findFirst()
                                            .get(),
                                        null
                                    ))
                            )
                            for (productVariantAttributeValue2 in savedProductVariantAttributeValue) {
                                if (attributeValue1!!.value == productVariantAttributeValue2.attributeValue!!.value) {
                                    productVariantAttributeValue.add(productVariantAttributeValue2)
                                }
                            }
                        }
                        if (variancesImagesMap.isNotEmpty()) { saveVarianceImage(variancesImagesMap[image]) }
                        val productVariantItemSaved = productVariantRepository.save(
                            ProductVariant(
                                null,
                                null,
                                productVariantAttributeValue,
                                HashSet(),
                                HashSet(),
                                HashSet(),
                                price,
                                unit,
                                image
                            )
                        )
                        println("**********$productVariantItemSaved")
                        for (productVariantAttributeValue1 in productVariantAttributeValue) {
                            productVariantAttributeValue1.productVariant = productVariantItemSaved
                            productVariantAttributeValueRepository.save(productVariantAttributeValue1)
                        }
                        savedProductVariant.add(productVariantItemSaved)
                    }
                    val savedProduct = productRepository.save(
                        Product(
                            null,
                            product.name,
                            product.description,
                            product.tags,
                            HashSet(),  //product.getCategories(), todo: see with Product categories
                            customCategoryService.findById(product.customCategory!!.id!!),
                            product.images,
                            product.productVariants,
                            product.attributes,
                            false
                        )
                    )
                    val images = product.images.stream()
                        .map(Image::content)
                        .map { content: String? ->
                            Image(
                                null,
                                content,
                                null
                            )
                        }.collect(Collectors.toList())
                    for (image in images) {
                        image.product = savedProduct
                        imageRepository.save(image)
                    }
                    for (productVariant in savedProductVariant) {
                        productVariant.product = savedProduct
                        productVariantRepository.save(productVariant)
                    }
                    savedProduct
                }
                .map { product: Product? -> productMapper!!.toDTO(product) }
                .orElse(null)
        } else null
    }

    fun downloadImage(filename: String?): ByteArray {
        return fileStorage!!.download(filename, "smartCity-files")
    }

    fun create(
        productDTO: ProductDTO,
        productImages: List<MultipartFile>?,
        variancesImages: List<MultipartFile>?
    ): ProductDTO? {
        println(productDTO.toString())
        productImages?.let { saveProductImages(it) }
        val variancesImagesMap: MutableMap<String?, MultipartFile> = HashMap()
        if (variancesImages != null) {
            for (image in variancesImages) {
                variancesImagesMap[image.originalFilename] = image
            }
        }
        return Optional.of(productDTO)
            .map { productDTO: ProductDTO? -> productMapper!!.toModel(productDTO) }
            .map { product: Product? ->
                val savedAttributes = product!!.attributes
                    .stream()
                    .map { (_, name): Attribute ->
                        attributeRepository.save(
                            Attribute(null, name, HashSet())
                        )
                    }
                    .collect(Collectors.toSet())
                val savedAttributeValue: MutableSet<AttributeValue> = HashSet()
                for ((_, name, attributeValues) in product.attributes) {
                    for ((_, value) in attributeValues) {
                        savedAttributeValue.add(
                            attributeValueRepository.save(
                                AttributeValue(
                                    null,
                                    value,
                                    HashSet(),
                                    savedAttributes.stream()
                                        .filter { (_, name1): Attribute -> name1 == name }
                                        .findFirst()
                                        .get()
                                )))
                    }
                }
                val savedProductVariant: MutableList<ProductVariant> = ArrayList()
                for ((_, _, productVariantAttributeValuesProductVariant, _, _, _, price, unit, image) in product.productVariants) {
                    val productVariantAttributeValue: MutableList<ProductVariantAttributeValue> = ArrayList()
                    for ((_, attributeValue1) in productVariantAttributeValuesProductVariant) {
                        val savedProductVariantAttributeValue: MutableList<ProductVariantAttributeValue> = ArrayList()
                        savedProductVariantAttributeValue.add(
                            productVariantAttributeValueRepository.save(
                                ProductVariantAttributeValue(
                                    null,
                                    savedAttributeValue
                                        .stream()
                                        .filter { (_, value): AttributeValue -> value == attributeValue1!!.value }
                                        .findFirst()
                                        .get(),
                                    null
                                ))
                        )
                        for (productVariantAttributeValue2 in savedProductVariantAttributeValue) {
                            if (attributeValue1!!.value == productVariantAttributeValue2.attributeValue!!.value) {
                                productVariantAttributeValue.add(productVariantAttributeValue2)
                            }
                        }
                    }
                    if (!variancesImagesMap.isEmpty()) saveVarianceImage(variancesImagesMap[image])
                    val productVariantItemSaved = productVariantRepository.save(
                        ProductVariant(
                            null,
                            product,
                            productVariantAttributeValue,
                            HashSet(),
                            HashSet(),
                            HashSet(),
                            price,
                            unit,
                            image
                        )
                    )
                    println("**********$productVariantItemSaved")
                    for (productVariantAttributeValue1 in productVariantAttributeValue) {
                        productVariantAttributeValue1.productVariant = productVariantItemSaved
                        productVariantAttributeValueRepository.save(productVariantAttributeValue1)
                    }
                    savedProductVariant.add(
                        productVariantItemSaved
                    )
                }
                val savedProduct = productRepository.save(
                    Product(
                        null,
                        product.name,
                        product.description,
                        product.tags,
                        HashSet(),
                        customCategoryService.findById(product.customCategory!!.id!!),
                        product.images,
                        savedProductVariant,
                        savedAttributes,
                        false
                    )
                )
                val images = product.images.stream()
                    .map(Image::content)
                    .map { content: String? ->
                        Image(
                            null,
                            content,
                            null
                        )
                    }.collect(Collectors.toList())
                for (image in images) {
                    image.product = savedProduct
                    imageRepository.save(image)
                }
                for (productVariant in savedProductVariant) {
                    productVariant.product = savedProduct
                    productVariantRepository.save(productVariant)
                }
                savedProduct
            }
            .map { product: Product? -> productMapper!!.toDTO(product) }
            .orElse(null)
    }

    private fun saveVarianceImage(image: MultipartFile?) {
        try {
            if (image != null) {
                fileStorage!!.upload(
                    image.originalFilename,
                    "smartCity-files", image.inputStream
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw FileStorageException("error.file.upload")
        }
    }

    private fun saveProductImages(images: List<MultipartFile>) {
        try {
            for (image in images) {
                fileStorage!!.upload(
                    image.originalFilename,
                    "smartCity-files", image.inputStream
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw FileStorageException("error.file.upload")
        }
    }
}