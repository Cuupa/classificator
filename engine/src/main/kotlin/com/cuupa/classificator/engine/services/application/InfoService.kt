package com.cuupa.classificator.engine.services.application

import com.cuupa.classificator.engine.extensions.Extension.substringBetween
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

class InfoService {

    @Value("\${server.port:8080}")
    private var serverport: String? = null

    private var version: String? = null

    fun getVersion(): String {
        if (version.isNullOrEmpty()) {
            version = getVersionFromSpringBoot()
        }

        return version ?: "unknown"
    }

    private fun getVersionFromSpringBoot(): String? {
        val template = RestTemplate()
        val url = "http://localhost:$serverport/actuator/info"
        try {
            val response = template.getForEntity(url, String::class.java)
            if (response.statusCode.is2xxSuccessful) {
                return tryGetVersion(response.body)
            }
        } catch (e: HttpStatusCodeException) {

        }
        return null
    }

    private fun tryGetVersion(response: String?): String? {
        if (response.isNullOrEmpty()) {
            return null
        }
        try {
            val version = response.substringBetween("version\":\"", "\",")
            if (version.isNullOrEmpty()){
                return null
            }
        } catch (e: Exception) {
            log.error("Failed to determine version number from $response", e)
        }
        return null
    }

    companion object {
        private val log = LogFactory.getLog(InfoService::class.java)
    }
}