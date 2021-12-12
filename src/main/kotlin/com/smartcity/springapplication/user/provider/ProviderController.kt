package com.smartcity.springapplication.user.provider

import com.smartcity.springapplication.user.UserDTO
import com.smartcity.springapplication.user.UserRegistrationDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/provider")
class ProviderController @Autowired constructor(
    val providerService: ProviderService
) {
    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun registerUser(@ModelAttribute userDTO: UserDTO): UserRegistrationDTO = providerService.saveUser(userDTO)!!
}