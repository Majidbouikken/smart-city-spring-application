package com.smartcity.springapplication.product

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ctr {
    @RequestMapping("/operations")
    fun test(): String = "index"
}