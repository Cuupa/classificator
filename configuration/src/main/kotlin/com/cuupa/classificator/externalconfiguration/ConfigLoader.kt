package com.cuupa.classificator.externalconfiguration

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class ConfigLoader(private val jackson: ObjectMapper, private val configFile: File) {

    fun getConfig(): Config {
        if(configFile.exists()) {
            return jackson.readValue(configFile, Config::class.java)
        }
        return Config()
    }
}
