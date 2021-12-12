package com.smartcity.springapplication

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmartcitySpringApplication

fun main(args: Array<String>) {
	runApplication<SmartcitySpringApplication>(*args)
}
