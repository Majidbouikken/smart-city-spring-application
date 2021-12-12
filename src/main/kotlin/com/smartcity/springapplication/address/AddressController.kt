package com.smartcity.springapplication.address

import com.smartcity.springapplication.utils.Response
import com.smartcity.springapplication.utils.Results
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/address")
class AddressController @Autowired constructor(
    val addressService: AddressService
) {
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    fun addAddressToUser(@RequestBody addressDTO: AddressDTO?): Response<String?>? =
        addressService.createAddress(addressDTO!!)

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteAddressToUser(@PathVariable(name = "id") addressId: Long?): Response<String> =
        addressService.deleteAddress(addressId!!)

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getUserAddress(@PathVariable(name = "id") userId: Long?): Results<AddressDTO?> =
        Results(addressService.getUserAddress(userId))
}