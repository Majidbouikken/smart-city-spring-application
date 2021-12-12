package com.smartcity.springapplication.store

import com.smartcity.springapplication.store.exception.CustomCategoryNotFoundException
import com.smartcity.springapplication.store.exception.DefaultCategoryNotFoundException
import com.smartcity.springapplication.store.exception.MultipleStoreException
import com.smartcity.springapplication.store.exception.StoreNotFoundException
import com.smartcity.springapplication.user.provider.ProviderNotFoundException
import com.smartcity.springapplication.utils.GenericErrorResponse
import com.smartcity.springapplication.utils.ImageStoreNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.Exception

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(ProviderNotFoundException::class)
    fun handleProviderNotFoundException(exception: Exception?, request: WebRequest?): ResponseEntity<Any> =
        returnErrorMessage("ProviderNotFound", HttpStatus.NOT_FOUND)

    @ExceptionHandler(DefaultCategoryNotFoundException::class)
    fun handleCategoryStoreNotFoundException(exception: Exception?, request: WebRequest?): ResponseEntity<Any> =
        returnErrorMessage("CategoryStoreNotFound", HttpStatus.NOT_FOUND)

    @ExceptionHandler(ImageStoreNotFoundException::class)
    fun handleImageStoreNotFoundException(exception: Exception?, request: WebRequest?): ResponseEntity<Any> =
        returnErrorMessage("ImageStoreNotFound", HttpStatus.NOT_FOUND)

    @ExceptionHandler(StoreNotFoundException::class)
    fun handleStoreNotFoundException(exception: Exception?, request: WebRequest?): ResponseEntity<Any> =
        returnErrorMessage("StoreNotFound", HttpStatus.NOT_FOUND)

    @ExceptionHandler(MultipleStoreException::class)
    fun handleMultipleStoreException(exception: Exception?, request: WebRequest?): ResponseEntity<Any> =
        returnErrorMessage("MultipleStore", HttpStatus.BAD_REQUEST)

    @ExceptionHandler(CustomCategoryNotFoundException::class)
    fun handleCustomCategoryNotFoundException(exception: Exception?, request: WebRequest?): ResponseEntity<Any> =
        returnErrorMessage("CustomCategoryNotFound", HttpStatus.NOT_FOUND)

    private fun returnErrorMessage(errorMessage: String, httpStatus: HttpStatus): ResponseEntity<Any> =
        ResponseEntity<Any>(GenericErrorResponse(errorMessage), HttpHeaders(), httpStatus)
}