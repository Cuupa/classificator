package com.cuupa.classificator.configuration

import com.cuupa.classificator.configuration.external.ConfigLoader
import com.cuupa.classificator.configuration.external.Config
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ExternalConfiguration {

    @Bean
    open fun jackson(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).apply {
            enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT); disable(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
        )
        }
    }

    @Bean
    open fun configuration(): Config? {
        return ConfigLoader(jackson()).getConfig()
    }
}
