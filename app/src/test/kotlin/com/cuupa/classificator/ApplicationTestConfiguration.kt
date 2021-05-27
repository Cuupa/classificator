package com.cuupa.classificator

import com.cuupa.classificator.monitor.configuration.MonitorConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [EngineTestConfiguration::class, MonitorTestConfiguration::class])
open class ApplicationTestConfiguration {

}
