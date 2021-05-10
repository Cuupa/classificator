package com.cuupa.classificator.configuration

import com.cuupa.classificator.configuration.external.Config
import com.cuupa.classificator.monitor.EventStorage
import com.cuupa.classificator.monitor.Monitor
import com.cuupa.classificator.monitor.sqlite.EventRepository
import com.cuupa.classificator.monitor.sqlite.EventService
import com.cuupa.classificator.monitor.sqlite.SqliteEventStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import java.util.*
import javax.sql.DataSource

@Configuration
open class MonitorConfiguration {

    @Autowired
    private var configuration: Config? = null

    @Value("\${classificator.monitor.database-name}")
    private var databaseName: String? = null

    @Value("\${classificator.monitor.enabled}")
    private var enabled: Boolean? = null

    @Value("\${classificator.monitor.logText}")
    private var logText: Boolean? = null

    @Bean
    open fun monitor(eventStorage: EventStorage): Monitor {
        return Monitor(eventStorage, isEnabled(), isLogText())
    }

    @Bean
    open fun eventStorage(eventService: EventService): EventStorage {
        return SqliteEventStorage(eventService)
    }

    @Bean
    open fun eventService(eventRepository: EventRepository): EventService {
        return EventService(eventRepository)
    }

    @Bean
    open fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .driverClassName("org.sqlite.JDBC")
            .url("jdbc:sqlite:${getDatabaseName()}")
            .build()
    }

    @Bean
    open fun entityManagerFactory(dataSource: DataSource): LocalSessionFactoryBean? {
        return LocalSessionFactoryBean().apply {
            setDataSource(dataSource)
            setPackagesToScan("com.cuupa.classificator")
            hibernateProperties = hibernateProperties()
        }
    }

    private fun hibernateProperties(): Properties {
        return Properties().apply {
            setProperty("hibernate.hbm2ddl.auto", "update"); setProperty(
            "hibernate.dialect",
            "com.cuupa.classificator.monitor.sqlite.SqliteDialect"
        )
        }
    }

    private fun getDatabaseName(): String {
        return if (databaseName.isNullOrEmpty()) {
            configuration?.classificator?.monitorConfig?.databaseName ?: ""
        } else {
            databaseName ?: ""
        }
    }

    private fun isEnabled(): Boolean {
        return if (enabled == null) {
            configuration?.classificator?.monitorConfig?.enabled ?: false
        } else {
            enabled ?: false
        }
    }

    private fun isLogText(): Boolean {
        return if (logText == null) {
            configuration?.classificator?.monitorConfig?.logText ?: false
        } else {
            logText ?: false
        }
    }
}
