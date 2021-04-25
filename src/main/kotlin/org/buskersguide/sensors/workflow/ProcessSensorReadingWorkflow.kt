package org.buskersguide.sensors.workflow

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.buskersguide.sensors.model.SensorReading
import org.buskersguide.sensors.repository.SensorReadingRepository
import org.buskersguide.sensors.repository.SensorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.stereotype.Service
private val logger = KotlinLogging.logger {}

@Service
class ProcessSensorReadingWorkflow(
    @Autowired private val sensorReadingRepo: SensorReadingRepository,
    @Autowired private val sensorRepo: SensorRepository,
    @Autowired private val mapper: ObjectMapper
) {
    fun processReading(message: Message<String>) {
        logger.debug("received message ${message.payload}")

        val sensorReading =
             mapper.readValue(message.payload, SensorReading::class.java)
        if (isValid(sensorReading)) {
            sensorRepo.save(sensorReading.sensor)
            sensorReadingRepo.save(sensorReading)
        } else {
            // todo: handle error
            //       qos, dlq, "will" topic
        }
        logger.debug("message ${sensorReading?.id} processed")
    }

}


// validate
//  nothing much to validate. if it's non-valid, discard reading
// future: dlq, -Will, qos options
fun isValid(reading: SensorReading): Boolean = reading.sensor.name != null
