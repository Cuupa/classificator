package regressionTests.config

import com.cuupa.classificator.monitor.EventStorage
import com.cuupa.classificator.monitor.FileEventStorage
import com.cuupa.classificator.monitor.Monitor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MonitorTestConfiguration {

    @Bean
    open fun monitor(): Monitor {
        return Monitor(eventStorage())
    }

    @Bean
    open fun eventStorage(): EventStorage {
        return FileEventStorage()
    }
}
