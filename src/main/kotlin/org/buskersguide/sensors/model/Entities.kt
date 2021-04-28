package org.buskersguide.sensors.model

import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "sensor_reading")
data class SensorReading(
    @Id var id: UUID? = UUID.randomUUID(),
    @ManyToOne
    var sensor: Sensor = Sensor(),
    var type: String? = null,
    var value: Double? = null,
    var unit: String? = null,
    var timestamp: LocalDateTime? = LocalDateTime.now(),
)

@Entity
@Table(name = "sensor")
data class Sensor(
    var name: String? = null,
    @Id
    var id: UUID? = UUID.randomUUID()
)