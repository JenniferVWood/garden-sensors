package org.buskersguide.sensors.repository

import org.buskersguide.sensors.model.Sensor
import org.buskersguide.sensors.model.SensorReading
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SensorReadingRepository : CrudRepository<SensorReading, Long> {}

@Repository
interface SensorRepository : CrudRepository<Sensor, Long>{
    fun save(sensor: Sensor?)
}