package com.smartcity.springapplication.user.provider

import lombok.AllArgsConstructor
import com.smartcity.springapplication.user.provider.ProviderRepository
import com.smartcity.springapplication.user.provider.ProviderMapper
import com.smartcity.springapplication.user.UserRegistrationMapper
import com.smartcity.springapplication.user.UserDTO
import com.smartcity.springapplication.user.UserRegistrationDTO
import com.smartcity.springapplication.utils.error_handler.SimpleUserException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
@AllArgsConstructor
class ProviderService @Autowired constructor(val providerRepository: ProviderRepository) {
    private val providerMapper: ProviderMapper? = null
    private val userRegistrationMapper: UserRegistrationMapper? = null

    fun findById(id: Long): Provider = providerRepository.findById(id).orElseThrow { ProviderNotFoundException() }!!

    fun findByIdOptional(id: Long): Optional<Provider?> = providerRepository.findById(id)

    fun findProviderByEmail(email: String?): Optional<Provider?>? = providerRepository.findByEmail(email)

    fun findAllProviders(): List<Provider?> = providerRepository.findAll()

    fun isPresentProviderByEmail(email: String?): Boolean = providerRepository.findByEmail(email)!!.isPresent

    fun saveUser(userDTO: UserDTO): UserRegistrationDTO? = Optional.of(userDTO)
        .map { userDto: UserDTO? -> providerMapper!!.toModel(userDto) }
        .filter { user: Provider? -> !isPresentProviderByEmail(user!!.email) }
        .map { provider: Provider? -> providerRepository.save(provider!!) }
        .map { provider: Provider? -> providerMapper!!.toDTO(provider) }
        .map { userDto: UserDTO? -> userRegistrationMapper!!.toRegistrationDTO(userDto) }
        .orElseThrow { SimpleUserException("This email used before") }
}