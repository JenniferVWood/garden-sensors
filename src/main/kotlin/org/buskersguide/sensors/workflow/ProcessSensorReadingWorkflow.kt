package org.buskersguide.sensors.workflow

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.buskersguide.sensors.model.Sensor
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
        GlobalScope.launch {
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

}