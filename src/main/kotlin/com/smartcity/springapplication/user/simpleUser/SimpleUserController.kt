package com.smartcity.springapplication.user.simpleUser

import com.smartcity.springapplication.user.UserDTO
import com.smartcity.springapplication.user.UserRegistrationDTO
import com.smartcity.springapplication.utils.Results
import com.smartcity.springapplication.category.CategoryDTO
import com.smartcity.springapplication.flashDeal.FlashDealDTO
import com.smartcity.springapplication.product.ProductDTO
import com.smartcity.springapplication.user.simpleUser.city.CityDTO
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class SimpleUserController @Autowired constructor(
    val simpleUserService: SimpleUserService
) {
    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun registerUser(@ModelAttribute userDTO: UserDTO): UserRegistrationDTO = simpleUserService.saveUser(userDTO)!!

    @PostMapping("/interestCenter")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun setUserInterestCenter(@ModelAttribute simpleUserDTO: SimpleUserDTO): Response<String> =
        simpleUserService.setUserInterestCenter(simpleUserDTO)

    @GetMapping("/interestCenter/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun getUserInterestCenter(@PathVariable id: Long): Results<CategoryDTO?> =
        Results(simpleUserService.getUserInterestCenter(id))

    @PostMapping("/Information")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun setUserInformation(@RequestBody simpleUserInformationDTO: SimpleUserInformationDTO): Response<String> =
        simpleUserService.setUserInformation(simpleUserInformationDTO)

    @GetMapping("/Information/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun getUserInformation(@PathVariable(value = "id") id: Long): SimpleUserInformationDTO =
        simpleUserService.getUserInformation(id)!!

    @GetMapping("/flash")
    @ResponseStatus(value = HttpStatus.OK)
    fun getUserFlashDeals(
        @RequestParam(value = "id") id: Long?,
        @RequestParam(value = "date") date: String?
    ): Results<FlashDealDTO?> = Results(simpleUserService.getUserFlashDeals(id, date))

    @GetMapping("/offer/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun getUserProductOffers(@PathVariable(value = "id") id: Long?): Results<ProductDTO> =
        Results(simpleUserService.getUserProductOffers(id))

    @PostMapping("/follow/store/{id}/{idUser}")
    @ResponseStatus(value = HttpStatus.OK)
    fun followStore(
        @PathVariable(value = "id") id: Long?,
        @PathVariable(value = "idUser") userId: Long?
    ): Response<String> = simpleUserService.followStore(id, userId)

    @PostMapping("/stop-follow/store/{id}/{idUser}")
    @ResponseStatus(value = HttpStatus.OK)
    fun stopFollowingStore(
        @PathVariable(value = "id") id: Long?,
        @PathVariable(value = "idUser") userId: Long?
    ): Response<String> = simpleUserService.stopFollowingStore(id, userId)

    @GetMapping("/is-follow/store/{id}/{idUser}")
    @ResponseStatus(value = HttpStatus.OK)
    fun isFollowingStore(
        @PathVariable(value = "id") id: Long?,
        @PathVariable(value = "idUser") userId: Long?
    ): Response<String> = simpleUserService.isFollowingStore(id, userId)

    @PostMapping("/default-city")
    @ResponseStatus(value = HttpStatus.OK)
    fun setUserDefaultCity(@RequestBody cityDTO: CityDTO): Response<String> =
        simpleUserService.setUserDefaultCity(cityDTO)

    @GetMapping("/default-city")
    @ResponseStatus(value = HttpStatus.OK)
    fun getUserDefaultCity(@RequestParam(name = "id") id: Long): CityDTO = simpleUserService.getUserDefaultCity(id)!!
}