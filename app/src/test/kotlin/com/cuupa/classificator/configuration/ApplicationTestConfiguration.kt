package com.cuupa.classificator.configuration

import com.cuupa.classificator.monitor.configuration.MonitorConfiguration
import com.cuupa.classificator.trainer.configuration.TrainerConfiguration
import com.cuupa.classificator.ui.configuration.SecurityConfiguration
import com.google.gson.Gson
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [EngineTestConfiguration::class, MonitorConfiguration::class, TrainerConfiguration::class, SecurityConfiguration::class, ApiImplementationTestConfiguration::class])
@ComponentScan(basePackages = ["com.cuupa.classificator.ui"])
open class ApplicationTestConfiguration {

    @Bean
    open fun gson(): Gson {
        return Gson()
    }

    @Bean
    open fun servletWebServerFactory(): ServletWebServerFactory {
        return TomcatServletWebServerFactory()
    }
}
