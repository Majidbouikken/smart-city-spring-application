package com.smartcity.springapplication.product

import com.smartcity.springapplication.utils.Results
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/product")
class ProductController @Autowired constructor(
    val productSearchService: ProductSearchService,
    val productService: ProductService,
) {
    /*@GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun searchProduct(
        @RequestParam(name = "search", required = false) query: String?,
        @RequestParam(name = "page", defaultValue = "1", required = false) page: Int,
        @RequestParam(name = "id") userId: Long?
    ): Results<ProductDTO> =
        if (query != null && query != "") Results(
            productSearchService.search(
                userId,
                query,
                page
            )
        ) else Results(
            productSearchService.findProductAround(
                userId,
                page
            )
        )

    @GetMapping("/interest")
    @ResponseStatus(HttpStatus.OK)
    fun getProductInterest(
        @RequestParam(name = "page", defaultValue = "1", required = false) page: Int,
        @RequestParam(name = "id") userId: Long?
    ): Results<ProductDTO?> = Results(productSearchService.findProductAroundMayInterest(userId, page))*/

    @PostMapping(value = ["/create"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createProduct(
        @RequestPart(value = "product") productDTO: ProductDTO,
        @RequestPart(value = "productImagesFile", required = false) productImages: List<MultipartFile>?,
        @RequestPart(value = "variantesImagesFile", required = false) variantsImages: List<MultipartFile>?
    ): ProductDTO = productService.create(productDTO, productImages, variantsImages)!!

    @PutMapping(value = ["/update"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    fun updateProduct(
        @RequestPart(value = "product") productDTO: ProductDTO,
        @RequestPart(value = "productImagesFile", required = false) productImages: List<MultipartFile>?,
        @RequestPart(value = "variantesImagesFile", required = false) variantsImages: List<MultipartFile>?
    ): ProductDTO = productService.updateProduct(productDTO, productImages, variantsImages)!!

    @DeleteMapping("delete/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    fun deleteCustomCategory(@PathVariable id: Long?): Response<String> = productService.deleteProduct(id)

    @GetMapping("/all/category/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun getProductByCustomCategoryId(@PathVariable("id") id: Long): Results<ProductDTO?> =
        Results(productService.getProductByCustomCategoryId(id))

    @GetMapping("/all/provider/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun getProductByCustomCategoryStoreProviderId(@PathVariable("id") id: Long): Results<ProductDTO?> =
        Results(productService.getProductByCustomCategoryStoreProviderId(id))

    @GetMapping("/all/store/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun getProductByCustomCategoryStoreId(@PathVariable("id") id: Long): Results<ProductDTO?> =
        Results(productService.getProductByCustomCategoryStoreId(id))

    @GetMapping(value = ["/image/{filename}"], produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getProductImage(@PathVariable("filename") filename: String?): ByteArray = productService.downloadImage(filename)

    @PutMapping("/move-category")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateProductsCustomCategory(
        @RequestParam(name = "products") productIds: List<Long>,
        @RequestParam(name = "category") newCustomCategoryId: Long?
    ): Response<String> = productService.updateProductsCustomCategory(productIds, newCustomCategoryId)

    /*@PutMapping("/clicked")
    @ResponseStatus(value = HttpStatus.OK)
    fun saveClickedProduct(
        @RequestParam(name = "userId") id: Long,
        @RequestParam(name = "productId") productId: Long
    ): Response<String> = productSearchService.saveClickedProduct(id, productId)*/
}