package com.smartcity.springapplication.store

import com.smartcity.springapplication.category.Category
import lombok.AllArgsConstructor
import com.smartcity.springapplication.category.CategoryService
import com.smartcity.springapplication.storeAddress.StoreAddressMapper
import com.smartcity.springapplication.category.CategoryMapper
import com.smartcity.springapplication.storeAddress.StoreAddressDTO
import com.smartcity.springapplication.store.exception.StoreNotFoundException
import com.smartcity.springapplication.category.CategoryDTO
import com.smartcity.springapplication.storage.FileStorage
import org.springframework.web.multipart.MultipartFile
import com.smartcity.springapplication.store.exception.MultipleStoreException
import java.io.IOException
import com.smartcity.springapplication.storage.FileStorageException
import com.smartcity.springapplication.utils.Response
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier
import java.util.stream.Collectors

@Service
@AllArgsConstructor
class StoreService @Autowired constructor(
    val storeRepository: StoreRepository,
    val categoryService: CategoryService,
) {
    private val fileStorage: FileStorage? = null
    private val storeMapper: StoreMapper? = null
    private val storeAddressMapper: StoreAddressMapper? = null
    private val storeInformationMapper: StoreInformationMapper? = null
    private val categoryMapper: CategoryMapper? = null

    @Named("getStoreName")
    fun getStoreName(store: Store): String? = store.name

    @Named("getStoreAddress")
    fun getStoreAddress(store: Store): String? = store.storeAddress!!.fullAddress

    @Named("getStoreAddressLat")
    fun getStoreAddressLat(store: Store): Double? = store.storeAddress!!.latitude

    @Named("getStoreAddressLon")
    fun getStoreAddressLon(store: Store): Double? = store.storeAddress!!.longitude

    @Named("getStoreAddressUpdated")
    fun getStoreAddressUpdated(store: Store): StoreAddressDTO? = storeAddressMapper!!.toDTO(
        store.storeAddress
    )

    @Named("getStoreFollowers")
    fun getStoreFollowers(store: Store): Int = store.followers.size

    @Named("getStoreId")
    fun getStoreId(store: Store): Long? = store.id

    @Named("findStoreByProviderId")
    fun findStoreByProviderId(id: Long?): Store = storeRepository.findByProviderId(id)!!
        .orElseThrow(Supplier { StoreNotFoundException() })!!

    @Named("findStoreById")
    fun findStoreById(id: Long): Store = storeRepository.findById(id).orElseThrow { StoreNotFoundException() }!!

    fun saveStore(store: Store): Store = storeRepository.save(store)

    private fun hasStore(id: Long?): Boolean = storeRepository.findByProviderId(id)!!.isPresent

    fun setStoreCategory(providerId: Long?, categories: List<String?>?): Response<String> {
        if (categories != null && categories.isNotEmpty()) {
            val store = findStoreByProviderId(providerId)
            val collect = categories.stream()
                .map { name: String? -> categoryService.findCategoryByName(name) }
                .collect(Collectors.toSet())
            store.defaultCategories = collect
            storeRepository.save(store)
        }
        return Response("created.")
    }

    fun getStoreCategories(providerId: Long?): List<CategoryDTO?> {
        val store = storeRepository.findByProviderId(providerId)!!
            .orElse(null)
        return if (store != null) store.defaultCategories
            .stream()
            .map { category: Category? -> categoryMapper!!.toDTO(category) }
            .collect(Collectors.toList()) else ArrayList()
    }

    fun create(storeCreationDTO: StoreCreationDTO, storeImage: MultipartFile?): StoreCreationDTO? {
        storeImage?.let { saveStoreImage(it) }
        return Optional.of(storeCreationDTO)
            .map { storeCreationDTO: StoreCreationDTO? -> storeMapper!!.toModel(storeCreationDTO) }
            .filter { store: Store? ->
                !hasStore(
                    store!!.provider!!.id
                )
            }
            .map { store: Store? -> setStoreAddress(store) }
            .map { entity: Store? -> storeRepository.save(entity!!) }
            .map { store: Store? -> storeMapper!!.toDTOCreation(store) }
            .orElseThrow { MultipleStoreException() }
    }

    private fun saveStoreImage(image: MultipartFile?) {
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

    private fun setStoreAddress(store: Store?): Store {
        store!!.storeAddress!!.store = store
        return store
    }

    fun setStoreInformation(storeInformationDto: StoreInformationCreationDTO): Response<String> {
        val store = findStoreByProviderId(storeInformationDto.providerId)
        // store.setAddress(storeInformationDto.getAddress());
        store.telephoneNumber = storeInformationDto.telephoneNumber
        store.defaultTelephoneNumber = storeInformationDto.defaultTelephoneNumber
        val collect = storeInformationDto.defaultCategories!!.stream()
            .map { name: String? -> categoryService.findCategoryByName(name) }
            .collect(Collectors.toSet())
        store.defaultCategories = collect
        storeRepository.save(store)
        return Response("created.")
    }

    fun getStoreInformation(providerId: Long?): StoreInformationDTO? {
        return Optional.of(findStoreByProviderId(providerId))
            .map { store: Store? -> storeInformationMapper!!.toDTO(store) }
            .orElseThrow { StoreNotFoundException() }
    }

    fun getStoreInformationByStoreId(storeId: Long): StoreInformationDTO? {
        return Optional.of(findStoreById(storeId))
            .map { store: Store? -> storeInformationMapper!!.toDTO(store) }
            .orElseThrow { StoreNotFoundException() }
    }

    fun findStoreByDistance(latitude: Double, longitude: Double, distance: Double, category: String): List<StoreDTO?> {
        return storeRepository.findStoreAround(latitude, longitude, distance)!!
            .stream()
            .filter(Predicate { store: Store? -> checkCategory(store!!, category) })
            .map<StoreDTO?>(Function { store: Store? -> storeMapper!!.toDTO(store) })
            .collect(Collectors.toList())
    }

    private fun checkCategory(store: Store, category: String): Boolean {
        if (category == "") {
            return true
        }
        val savedCategory = categoryService.findCategoryByName(category)
        return store.defaultCategories.contains(savedCategory)
    }
}