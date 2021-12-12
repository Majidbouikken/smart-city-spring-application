package com.smartcity.springapplication.offer

import com.smartcity.springapplication.utils.Results
import com.smartcity.springapplication.product.ProductDTO
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/offer")
class OfferController @Autowired constructor(
    val offerService: OfferService
) {
    @PostMapping("/create")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createOffer(@RequestBody offerCreationDTO: OfferCreationDTO): Response<String> =
        offerService.createOffer(offerCreationDTO)

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    fun deleteOffer(@PathVariable id: Long?): Response<String> =
        offerService.deleteOffer(id)

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateOffer(@RequestBody offerCreationDTO: OfferCreationDTO): Response<String> =
        offerService.updateOffer(offerCreationDTO)

    @GetMapping("/current-provider-offers")
    @ResponseStatus(HttpStatus.OK)
    fun getOrdersByProviderId(
        @RequestParam("id") id: Long?,
        @RequestParam(value = "status", required = false) status: OfferState
    ): Results<OfferDTO?> =
        Results(offerService.getOffersByProviderId(id, status))

    @GetMapping("/search-offer")
    @ResponseStatus(HttpStatus.OK)
    fun searchOfferByPosition(
        @RequestParam(name = "latitude") latitude: Double,
        @RequestParam(name = "longitude") longitude: Double
    ): Results<ProductDTO> =
        Results(offerService.searchOfferByPosition(latitude, longitude))
}