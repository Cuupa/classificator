package com.cuupa.classificator.app

import com.cuupa.classificator.engine.configuration.EngineConfiguration
import com.cuupa.classificator.monitor.configuration.MonitorConfiguration
import com.cuupa.classificator.ui.configuration.SecurityConfiguration
import org.apache.commons.logging.LogFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import javax.annotation.PostConstruct

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = [EngineConfiguration::class, SecurityConfiguration::class, MonitorConfiguration::class])
@ComponentScan(basePackages = ["com.cuupa.classificator"])
open class ClassificatorApplication : SpringBootServletInitializer() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ClassificatorApplication>(*args)
        }

        private val log = LogFactory.getLog(ClassificatorApplication::class.java)
    }

    @PostConstruct
    fun configLoaded() {
       log.info("Loaded ${ClassificatorApplication::class.simpleName}")
    }
}