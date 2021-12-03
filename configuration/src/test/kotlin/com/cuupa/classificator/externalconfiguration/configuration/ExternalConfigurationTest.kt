package com.cuupa.classificator.externalconfiguration.configuration

import com.cuupa.classificator.externalconfiguration.Config
import com.cuupa.classificator.externalconfiguration.ConfigLoader
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
open class ExternalConfigurationTest {

    @Bean
    open fun jackson(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).apply {
            enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }

    @Bean("testConfig")
    open fun configurationPrimary(jackson: ObjectMapper): Config {
        return ConfigLoader(jackson, File("src/test/resources/configuration.yml")).getConfig()
    }
}