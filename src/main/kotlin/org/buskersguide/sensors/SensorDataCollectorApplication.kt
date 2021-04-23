package org.buskersguide.sensors

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class SensorDataCollectorApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        Thread.currentThread().join()
    }
}

fun main(args: Array<String>) {
    runApplication<SensorDataCollectorApplication>(*args)
}

