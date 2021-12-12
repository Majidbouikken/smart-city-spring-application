package com.smartcity.springapplication.nominatim

import com.smartcity.springapplication.utils.Results
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/nominatim")
class NominatimController @Autowired constructor(
    var nominatimService: NominatimService
) {
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    fun getCityInformation(
        @RequestParam(name = "country") country: String?,
        @RequestParam(name = "city") city: String?
    ): Results<City> = Results(nominatimService.getCityInformation(country, city))
}