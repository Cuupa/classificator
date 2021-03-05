package regressionTests.config

import com.cuupa.classificator.monitor.EventStorage
import com.cuupa.classificator.monitor.Monitor
import com.cuupa.classificator.monitor.sqlite.EventRepository
import com.cuupa.classificator.monitor.sqlite.EventService
import com.cuupa.classificator.monitor.sqlite.SqliteEventStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import java.util.*
import javax.sql.DataSource

@Configuration
open class MonitorTestConfiguration {

    @Autowired
    private var repository: EventRepository? = null

    @Bean
    open fun monitor(): Monitor {
        return Monitor(eventStorage())
    }

    @Bean
    open fun eventStorage(): EventStorage {
        return SqliteEventStorage(eventService())
    }

    @Bean
    open fun eventService(): EventService {
        return EventService(repository!!)
    }

    @Bean
    open fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .driverClassName("org.sqlite.JDBC")
            .url("jdbc:sqlite:monitor.db")
            .build()
    }

    @Bean
    open fun entityManagerFactory(): LocalSessionFactoryBean? {
        val sessionFactory = LocalSessionFactoryBean()
        sessionFactory.setDataSource(dataSource())
        sessionFactory.setPackagesToScan("com.cuupa.classificator")
        sessionFactory.hibernateProperties = hibernateProperties()
        return sessionFactory
    }

    private fun hibernateProperties(): Properties {
        val properties = Properties()
        properties.setProperty("hibernate.dialect", "com.cuupa.classificator.monitor.sqlite.SqliteDialect")
        return properties
    }
}