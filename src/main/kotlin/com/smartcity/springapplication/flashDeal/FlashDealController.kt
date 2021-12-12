package com.smartcity.springapplication.flashDeal

import com.smartcity.springapplication.utils.Results
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/flashDeal")
class FlashDealController @Autowired constructor(
    val flashDealService: FlashDealService
) {
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    fun createFlashDeal(@RequestBody flashDealCreationDTO: FlashDealCreationDTO): Response<String> =
        flashDealService.createFlashDeal(flashDealCreationDTO)

    @GetMapping("/current-provider-flash/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getRecentFlashDealsStore(@PathVariable(value = "id") id: Long?): Results<FlashDealDTO> =
        Results(flashDealService.getRecentFlashDealsStore(id))

    @GetMapping("/current-provider-search-flash")
    @ResponseStatus(HttpStatus.OK)
    fun searchFlashDealsStore(
        @RequestParam(name = "id") id: Long,
        @RequestParam(name = "startDate") startDate: String?,
        @RequestParam(name = "endDate") endDate: String?
    ): Results<FlashDealDTO?> =
        Results(flashDealService.searchFlashDealsStore(id, startDate, endDate))

    @GetMapping("/search-flash")
    @ResponseStatus(HttpStatus.OK)
    fun searchFlashByPosition(
        @RequestParam(name = "latitude") latitude: Double,
        @RequestParam(name = "longitude") longitude: Double,
        @RequestParam(value = "date") date: String?
    ): Results<FlashDealDTO?> =
        Results(flashDealService.searchFlashByPosition(latitude, longitude, date))
}