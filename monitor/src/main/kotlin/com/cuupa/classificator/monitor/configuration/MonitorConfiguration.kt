package com.cuupa.classificator.monitor.configuration

import com.cuupa.classificator.externalconfiguration.Config
import com.cuupa.classificator.monitor.persistence.EventStorage
import com.cuupa.classificator.monitor.persistence.sqlite.EventRepository
import com.cuupa.classificator.monitor.persistence.sqlite.EventService
import com.cuupa.classificator.monitor.persistence.sqlite.SqliteEventStorage
import com.cuupa.classificator.monitor.service.Monitor
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import java.util.*
import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    basePackages = ["com.cuupa.classificator.monitor.persistence"],
    entityManagerFactoryRef = "monitor_entityManagerFactory",
    transactionManagerRef = "monitor_transaktionManager"
)
open class MonitorConfiguration {

    @Autowired
    private var configuration: Config? = null

    @Value("\${classificator.monitor.database_name}")
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

    @Bean("monitor_datasource")
    open fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .driverClassName("org.sqlite.JDBC")
            .url("jdbc:sqlite:${getDatabaseName()}")
            .build()
    }

    @Bean("monitor_entityManagerFactory")
    open fun entityManagerFactory(@Qualifier("monitor_datasource") dataSource: DataSource): LocalSessionFactoryBean? {
        return LocalSessionFactoryBean().apply {
            setDataSource(dataSource)
            setPackagesToScan("com.cuupa.classificator.monitor")
            hibernateProperties = hibernateProperties()
        }
    }

    @Bean("monitor_transaktionManager")
    open fun dbTransactionManager(@Qualifier("monitor_entityManagerFactory") entityManager: LocalSessionFactoryBean): PlatformTransactionManager {
        return JpaTransactionManager().apply {
            entityManagerFactory = entityManager.getObject()
        }
    }

    private fun hibernateProperties(): Properties {
        return Properties().apply {
            setProperty("hibernate.hbm2ddl.auto", "update")
            setProperty("hibernate.dialect", SqliteDialect::class.java.canonicalName)
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

    @PostConstruct
    fun configLoaded() {
        log.info("Loaded ${MonitorConfiguration::class.simpleName}")
    }

    companion object {
        private val log = LogFactory.getLog(MonitorConfiguration::class.java)
    }
}
