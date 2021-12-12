package com.smartcity.springapplication.address

import com.smartcity.springapplication.utils.Response
import com.smartcity.springapplication.utils.error_handler.AddressException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Supplier
import java.util.stream.Collectors
import javax.transaction.Transactional

@Service
class AddressService @Autowired constructor(
    val addressRepository: AddressRepository,
) {
    private val addressMapper: AddressMapper? = null

    @Transactional
    fun createAddress(addressDTO: AddressDTO): Response<String?>? {
        Optional.of<AddressDTO>(addressDTO)
            .map<Address>(addressMapper!!::toModel)
            .map(addressRepository::save)
            .orElseThrow(Supplier { AddressException("error.address.creation") })
        return Response("created.")
    }

    @Transactional
    fun deleteAddress(addressId: Long): Response<String> {
        val address = addressRepository.findById(addressId)
            .orElseThrow(Supplier<RuntimeException> { AddressException("error.address.notFound") })!!
        address.deleted = true
        addressRepository.save(address)
        return Response("deleted.")
    }

    fun getUserAddress(userId: Long?): MutableList<AddressDTO?>? =
        addressRepository.findByUserId(userId)!!.stream()
            .filter { address -> !address!!.deleted }
            .map(addressMapper!!::toDTO)
            .collect(Collectors.toList())
}