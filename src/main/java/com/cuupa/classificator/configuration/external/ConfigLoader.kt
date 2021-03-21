package com.cuupa.classificator.configuration.external

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class ConfigLoader(private val jackson: ObjectMapper) {

    fun getConfig(): Config? {
        return jackson.readValue(configFile, Config::class.java)
    }

    companion object {
        val configFile = File("configuration.yml")
    }
}
