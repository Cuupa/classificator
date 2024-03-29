package com.cuupa.classificator.externalconfiguration.configuration

import com.cuupa.classificator.externalconfiguration.Config
import com.cuupa.classificator.externalconfiguration.ConfigLoader
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.apache.commons.logging.LogFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.io.File

@Configuration
open class ExternalConfiguration {

    @Bean
    open fun jackson(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).apply {
            enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }

    @Bean
    @Primary
    open fun configuration(jackson: ObjectMapper): Config {
        val configFile = File("configuration.yml")
        return ConfigLoader(jackson, configFile).getConfig()
    }

    companion object {
        private val log = LogFactory.getLog(ExternalConfiguration::class.java)
    }
}