package com.cuupa.classificator

import com.cuupa.classificator.monitor.configuration.MonitorConfiguration
import com.cuupa.classificator.monitor.persistence.EventStorage
import com.cuupa.classificator.monitor.service.Monitor
import org.apache.commons.logging.LogFactory
import org.springframework.context.annotation.Bean
import javax.annotation.PostConstruct

class MonitorTestConfiguration {

    @Bean
    open fun monitor(eventStorage: EventStorage): Monitor {
        return Monitor(eventStorage, true, true)
    }

    @Bean
    open fun eventStorage(): EventStorage {
        return EventStorageMock()
    }

    @PostConstruct
    fun configLoaded() {
        log.info("Loaded ${MonitorConfiguration::class.simpleName}")
    }

    companion object {
        private val log = LogFactory.getLog(MonitorConfiguration::class.java)
    }
}
