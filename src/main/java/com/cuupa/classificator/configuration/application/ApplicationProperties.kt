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
    val metadataFiles: String? = null
    @Value("\${classificator.regexfiles}")
    val regexFiles: String? = null
    @Value("\${classificator.senderfiles}")
    val senderFiles: String? = null

    @PostConstruct
    fun init() {
        initServerEnvironment()
    }

    private fun initServerEnvironment() {
        if (System.getProperty("jboss.server.config.dir") != null) {
            setJBoss()
        } else if (System.getProperty("catalina.base") != null) {
            setTomcat()
        }
        LOG.debug("detected environment: " + serverProperties.getProperty("serverEnvironment"))
        LOG.debug("set config dir to : " + serverProperties.getProperty("configDir"))
    }

    private fun setTomcat() {
        serverProperties.setProperty("serverEnvironment", "tomcat")
        serverProperties.setProperty("configDir", System.getProperty("catalina.base") + fileSeperator + "conf"
                + fileSeperator + "classificator" + fileSeperator)
    }

    private fun setJBoss() {
        serverProperties.setProperty("serverEnvironment", "jboss")
        serverProperties.setProperty("configDir", System.getProperty("jboss.server.config.dir") + fileSeperator
                + "classificator" + fileSeperator)
    }

    private val fileSeperator: String
        get() = System.getProperty("file.separator")

    companion object {
        private val LOG = LogFactory.getLog(ApplicationProperties::class.java)
    }
}