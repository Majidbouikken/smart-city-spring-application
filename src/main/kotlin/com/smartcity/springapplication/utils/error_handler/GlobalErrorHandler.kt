package com.smartcity.springapplication.utils.error_handler

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalErrorHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(SimpleUserException::class)
    fun handleSimpleUserException(e: SimpleUserException): ResponseEntity<Any> =
        returnErrorMessage(e.message, HttpStatus.NOT_FOUND)


    @ExceptionHandler(CartException::class)
    fun handleCartException(e: CartException): ResponseEntity<Any> =
        returnErrorMessage(e.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(DateException::class)
    fun handleCartException(e: DateException): ResponseEntity<Any> =
        returnErrorMessage(e.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(OrderException::class)
    fun handleCartException(e: OrderException): ResponseEntity<Any> =
        returnErrorMessage(e.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(PoliticsException::class)
    fun handleCartException(e: PoliticsException): ResponseEntity<Any> =
        returnErrorMessage(e.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(AddressException::class)
    fun handleCartException(e: AddressException): ResponseEntity<Any> =
        returnErrorMessage(e.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(OfferException::class)
    fun handleCartException(e: OfferException): ResponseEntity<Any> =
        returnErrorMessage(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(FlashDealException::class)
    fun handleFlashDealException(e: FlashDealException): ResponseEntity<Any> =
        returnErrorMessage(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(CategoryException::class)
    fun handleCategoryException(e: CategoryException): ResponseEntity<Any> =
        returnErrorMessage(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(ProductException::class)
    fun handleProductException(e: ProductException): ResponseEntity<Any> =
        returnErrorMessage(e.message, HttpStatus.NOT_FOUND)

    private fun returnErrorMessage(errorMessage: String?, httpStatus: HttpStatus): ResponseEntity<Any> =
        ResponseEntity<Any>(errorMessage, HttpHeaders(), httpStatus)
}