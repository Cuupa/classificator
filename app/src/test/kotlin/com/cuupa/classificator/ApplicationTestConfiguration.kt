package com.cuupa.classificator

import com.cuupa.classificator.monitor.configuration.MonitorConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [EngineTestConfiguration::class, MonitorConfiguration::class, ExternalTestConfiguration::class])
open class ApplicationTestConfiguration
