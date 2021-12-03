package com.cuupa.classificator.trainer.configuration

import com.cuupa.classificator.externalconfiguration.Config
import com.cuupa.classificator.trainer.persistence.DocumentStorage
import com.cuupa.classificator.trainer.persistence.sqlite.DocumentRepository
import com.cuupa.classificator.trainer.persistence.sqlite.SqliteDocumentStorage
import com.cuupa.classificator.trainer.services.DocumentService
import com.cuupa.classificator.trainer.services.Trainer
import com.cuupa.classificator.trainer.services.statistics.Precision
import com.cuupa.classificator.trainer.services.statistics.Recall
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
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
    basePackages = ["com.cuupa.classificator.trainer.persistence"],
    entityManagerFactoryRef = "trainer_entityManagerFactory",
    transactionManagerRef = "trainer_transactionManager"
)
open class TrainerConfiguration {

    @Autowired
    private var configuration: Config? = null

    @Bean
    open fun trainer(service: DocumentService): Trainer {
        return Trainer(service)
    }

    @Bean
    open fun documentStorage(repository: DocumentRepository): DocumentStorage {
        return SqliteDocumentStorage(repository)
    }

    @Bean
    open fun documentService(storage: DocumentStorage, precision: Precision, recall: Recall): DocumentService {
        return DocumentService(storage, precision, recall)
    }

    @Bean
    open fun recall(): Recall {
        return Recall()
    }

    @Bean
    open fun precision(): Precision {
        return Precision()
    }

    @Bean("trainer_datasource")
    open fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .driverClassName("org.sqlite.JDBC")
            .url("jdbc:sqlite:${getDatabaseName()}")
            .build()
    }

    @Bean("trainer_entityManagerFactory")
    open fun entityManagerFactory(@Qualifier("trainer_datasource") dataSource: DataSource): LocalSessionFactoryBean? {
        return LocalSessionFactoryBean().apply {
            setDataSource(dataSource)
            setPackagesToScan("com.cuupa.classificator.trainer")
            hibernateProperties = hibernateProperties()
        }
    }

    @Bean("trainer_transactionManager")
    open fun dbTransactionManager(@Qualifier("trainer_entityManagerFactory") entityManager: LocalSessionFactoryBean): PlatformTransactionManager {
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

    private fun getDatabaseName() = configuration?.classificator?.trainerConfig?.databaseName ?: ""

    @PostConstruct
    fun configLoaded() {
        log.info("Loaded ${TrainerConfiguration::class.simpleName}")
    }

    companion object {
        private val log = LogFactory.getLog(TrainerConfiguration::class.java)
    }
}
