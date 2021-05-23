package com.cuupa.classificator.app

import com.cuupa.classificator.engine.configuration.EngineConfiguration
import com.cuupa.classificator.monitor.configuration.MonitorConfiguration
import com.cuupa.classificator.ui.configuration.SecurityConfiguration
import org.apache.commons.logging.LogFactory
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import javax.annotation.PostConstruct

/**
 * @author Simon Thiel (https://github.com/cuupa) (19.05.2021)
 */
@Configuration
@Import(value = [EngineConfiguration::class, SecurityConfiguration::class, MonitorConfiguration::class])
@ComponentScan(basePackages = ["com.cuupa.classificator"])
open class ApplicationConfiguration {

    @PostConstruct
    fun configLoaded() {
        log.info("Loaded ${ApplicationConfiguration::class.simpleName}")
    }

    companion object {
        private val log = LogFactory.getLog(ApplicationConfiguration::class.java)
    }
}