package org.buskersguide.sensors

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import java.util.*


@SpringBootApplication
class SensorDataCollectorApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        Thread.currentThread().join()
    }
}

fun main(args: Array<String>) {
    runApplication<SensorDataCollectorApplication>(*args)
}

