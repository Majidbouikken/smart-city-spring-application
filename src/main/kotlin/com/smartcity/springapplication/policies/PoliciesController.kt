package com.smartcity.springapplication.policies

import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/policy")
class PoliciesController @Autowired constructor(
    val policiesService: PoliciesService
) {
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    fun createPolitics(@RequestBody policiesDTO: PoliciesDTO): Response<String> =
        policiesService.createPolitics(policiesDTO)

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPolitics(@PathVariable id: Long): PoliciesDTO =
        policiesService.getPolitics(id)

    @GetMapping("/store/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getStorePolitics(@PathVariable id: Long): PoliciesDTO =
        policiesService.getStorePolitics(id)
}