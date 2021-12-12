package com.smartcity.springapplication.bill

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bill")
class BillController @Autowired constructor(
    val billService: BillService
) {
    @PostMapping("/total")
    @ResponseStatus(HttpStatus.OK)
    fun getTotalToPay(@RequestBody billTotalDTO: BillTotalDTO?): BillTotalDTO =
        billService.getTotalToPay(billTotalDTO!!)
}