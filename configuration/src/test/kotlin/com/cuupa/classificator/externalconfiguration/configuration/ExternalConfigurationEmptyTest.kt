package com.cuupa.classificator.externalconfiguration.configuration

import com.cuupa.classificator.externalconfiguration.Config
import com.cuupa.classificator.externalconfiguration.ConfigLoader
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.io.File

@Configuration
@Profile("no-file")
open class ExternalConfigurationEmptyTest {

    @Bean
    fun jackson(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).apply {
            enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }

    @Bean
    fun configuration(jackson: ObjectMapper): Config {
        return ConfigLoader(jackson, File("")).getConfig()
    }
}