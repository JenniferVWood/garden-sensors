package org.buskersguide.sensors.workflow

import com.fasterxml.jackson.databind.ObjectMapper
import org.buskersguide.sensors.model.SensorReading
import org.buskersguide.sensors.repository.SensorReadingRepository
import org.buskersguide.sensors.repository.SensorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.integration.support.json.JsonObjectMapper
import org.springframework.messaging.Message
import org.springframework.stereotype.Service

@Service
class ProcessSensorReadingWorkflow(
    @Autowired private final val sensorReadingRepo: SensorReadingRepository,
    @Autowired private final val sensorRepo: SensorRepository,
    private final val mapper: ObjectMapper) {

    fun processReading(message: Message<String>) {
        // map to a model
        val sensorReading: SensorReading = mapper.readValue(message.payload, SensorReading::class.java)
        // validate

        // persist
        sensorRepo.save(sensorReading.sensor)
        sensorReadingRepo.save(sensorReading)

        println(message.payload)
    }
}