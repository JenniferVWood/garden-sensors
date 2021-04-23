package org.buskersguide.sensors.workflow

import com.fasterxml.jackson.databind.ObjectMapper
import org.buskersguide.sensors.model.Sensor
import org.buskersguide.sensors.model.SensorReading
import org.buskersguide.sensors.repository.SensorReadingRepository
import org.buskersguide.sensors.repository.SensorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.stereotype.Service

@Service
class ProcessSensorReadingWorkflow(
    @Autowired private val sensorReadingRepo: SensorReadingRepository,
    @Autowired private val sensorRepo: SensorRepository,
    private val mapper: ObjectMapper
) {

    suspend fun processReading(message: Message<String>) {
        val sensorReading: SensorReading = mapper.readValue(message.payload, SensorReading::class.java)
        println(message.payload)
        // validate
        //  nothing to validate. if it's in-valid, discard reading
        // future: dlq, -Will, qos options

        // persist
        val sensor: Sensor? = sensorReading.sensor
        if (sensor != null) {
            sensorRepo.save(sensor)
            sensorReadingRepo.save(sensorReading)

        }

    }

}