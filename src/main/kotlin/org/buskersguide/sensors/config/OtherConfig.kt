package org.buskersguide.sensors.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OtherConfig {
 @Bean
 fun getObjectMapper(): ObjectMapper {
  return ObjectMapper()
 }
}