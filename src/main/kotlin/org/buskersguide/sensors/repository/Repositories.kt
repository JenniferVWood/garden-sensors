package org.buskersguide.sensors.repository

import org.buskersguide.sensors.model.Sensor
import org.buskersguide.sensors.model.SensorReading
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SensorReadingRepository : CrudRepository<SensorReading, UUID>

@Repository
interface SensorRepository : CrudRepository<Sensor, UUID>
