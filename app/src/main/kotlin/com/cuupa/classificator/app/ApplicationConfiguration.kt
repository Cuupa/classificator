package com.cuupa.classificator.app

import com.cuupa.classificator.engine.configuration.EngineConfiguration
import com.cuupa.classificator.monitor.configuration.MonitorConfiguration
import com.cuupa.classificator.trainer.configuration.TrainerConfiguration
import com.cuupa.classificator.ui.configuration.SecurityConfiguration
import com.google.gson.Gson
import org.apache.commons.logging.LogFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import javax.annotation.PostConstruct

/**
 * @author Simon Thiel (https://github.com/cuupa) (19.05.2021)
 */
@Configuration
@Import(value = [EngineConfiguration::class, SecurityConfiguration::class, MonitorConfiguration::class, TrainerConfiguration::class])
@ComponentScan(basePackages = ["com.cuupa.classificator"])
open class ApplicationConfiguration {

    @Bean
    open fun gson(): Gson{
        return Gson()
    }

    @PostConstruct
    fun configLoaded() {
        log.info("Loaded ${ApplicationConfiguration::class.simpleName}")
    }

    companion object {
        private val log = LogFactory.getLog(ApplicationConfiguration::class.java)
    }
}