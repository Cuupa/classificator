package com.cuupa.classificator.configuration

import com.cuupa.classificator.api_implementation.api_key.ApiKeyValidator
import com.cuupa.classificator.api_implementation.api_key.configuration.SqliteDialect
import com.cuupa.classificator.api_implementation.api_key.repository.ApiKeyRepository
import com.cuupa.classificator.externalconfiguration.Config
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import java.util.*
import javax.sql.DataSource

@Configuration
@Import(value = [ExternalTestConfiguration::class])
@EnableJpaRepositories(
    basePackages = ["com.cuupa.classificator.api_implementation"],
    entityManagerFactoryRef = "api_entityManagerFactory",
    transactionManagerRef = "api_transactionManager"
)
open class ApiImplementationTestConfiguration {

    @Value("\${classificator.api_repository.database_name}")
    private var databaseName: String? = null

    @Bean
    open fun apiKeyValidator(config: Config, apiKeyRepository: ApiKeyRepository): ApiKeyValidator {
        return ApiKeyValidator(config, apiKeyRepository)
    }

    @Bean("api_datasource")
    open fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .driverClassName("org.sqlite.JDBC")
            .url("jdbc:sqlite:$databaseName")
            .build()
    }

    @Bean("api_entityManagerFactory")
    open fun entityManagerFactory(@Qualifier("api_datasource") dataSource: DataSource): LocalSessionFactoryBean? {
        return LocalSessionFactoryBean().apply {
            setDataSource(dataSource)
            setPackagesToScan("com.cuupa.classificator.api_implementation.api_key")
            hibernateProperties = hibernateProperties()
        }
    }

    @Bean("api_transactionManager")
    open fun dbTransactionManager(@Qualifier("api_entityManagerFactory") entityManager: LocalSessionFactoryBean): PlatformTransactionManager {
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
}
