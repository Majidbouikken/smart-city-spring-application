package com.smartcity.springapplication.processor

import org.springframework.batch.core.Job
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.boot.CommandLineRunner
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
@EnableScheduling
class ConfirmOrderBatchRunner : CommandLineRunner {
    var jobLauncher: JobLauncher? = null
    var job: Job? = null
    @Scheduled(cron = "0 56 11 * * *")
    @Throws(Exception::class)
    fun perform() {
        val params = JobParametersBuilder()
            .addString("JobID", System.currentTimeMillis().toString())
            .toJobParameters()
        jobLauncher!!.run(job!!, params)
    }

    @Throws(Exception::class)
    override fun run(vararg args: String) {
    }
}