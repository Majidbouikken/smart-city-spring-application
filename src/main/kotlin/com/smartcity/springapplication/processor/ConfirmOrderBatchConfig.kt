package com.smartcity.springapplication.processor

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/*@Configuration
@EnableBatchProcessing
class ConfirmOrderBatchConfig @Autowired constructor(val orderService: OrderService) {
    private val jobs: JobBuilderFactory? = null
    private val steps: StepBuilderFactory? = null

    @Bean
    fun confirmReceiveOrder(): Step = steps!!["confirmReceiveOrder"]
        .tasklet(AutoConfirmReceiveOrder(orderService))
        .build()

    @Bean
    fun demoJob(): Job = jobs!!["demoJob"]
        .incrementer(RunIdIncrementer())
        .start(confirmReceiveOrder())
        .build()
}*/