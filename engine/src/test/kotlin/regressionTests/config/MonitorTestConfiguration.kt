package regressionTests.config

import com.cuupa.classificator.monitor.persistence.EventStorage
import com.cuupa.classificator.monitor.persistence.MockEventStorage
import com.cuupa.classificator.monitor.service.Monitor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MonitorTestConfiguration {

    @Bean
    fun monitor(): Monitor {
        return Monitor(eventStorage(), enabled = true, logText = true)
    }

    @Bean
    fun eventStorage(): EventStorage {
        return MockEventStorage()
    }
}