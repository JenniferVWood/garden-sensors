package org.buskersguide.sensors.config

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.buskersguide.sensors.workflow.ProcessSensorReadingWorkflow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.messaging.Message


@Configuration
class MqttListenerConfig(@Autowired private val processSensorReading: ProcessSensorReadingWorkflow) {

    @Bean
    fun mqttInbound(): IntegrationFlow {
        return IntegrationFlows.from(
            MqttPahoMessageDrivenChannelAdapter("tcp://localhost:1883", "testClient", "topic1", "topic2")
        )
            .handle { m: Message<String> ->
                processSensorReading.processReading(m)
            }
            .get()

    }
}