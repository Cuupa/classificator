package com.cuupa.classificator.configuration.application

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import java.util.*
import javax.annotation.PostConstruct

class ApplicationProperties {
    private val serverProperties = Properties()
    @Value("\${classificator.kbfiles}")
    val knowledgbaseDir: String? = null
    @Value("\${classificator.metadatafiles}")
    private val metadataFiles: String? = null
    @Value("\${classificator.regexfiles}")
    private val regexFiles: String? = null
    @Value("\${classificator.senderfiles}")
    val senderFiles: String? = null

    @PostConstruct
    fun init() {
        _initServerEnvironment()
    }

    private fun _initServerEnvironment() {
        if (System.getProperty("jboss.server.config.dir") != null) {
            _setJBoss()
        } else if (System.getProperty("catalina.base") != null) {
            _setTomcat()
        }
        LOG.debug("detected environment: " + serverProperties.getProperty("serverEnvironment"))
        LOG.debug("set config dir to : " + serverProperties.getProperty("configDir"))
    }

    private fun _setTomcat() {
        serverProperties.setProperty("serverEnvironment", "tomcat")
        serverProperties.setProperty("configDir", System.getProperty("catalina.base") + fileSeperator + "conf"
                + fileSeperator + "classificator" + fileSeperator)
    }

    private fun _setJBoss() {
        serverProperties.setProperty("serverEnvironment", "jboss")
        serverProperties.setProperty("configDir", System.getProperty("jboss.server.config.dir") + fileSeperator
                + "classificator" + fileSeperator)
    }

    val fileSeperator: String
        get() = System.getProperty("file.separator")

    companion object {
        private val LOG = LogFactory.getLog(ApplicationProperties::class.java)
    }
}