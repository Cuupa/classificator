package com.cuupa.classificator.configuration.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Properties;

public class ApplicationProperties {

	private static final Log LOG = LogFactory.getLog(ApplicationProperties.class);

	private final Properties serverProperties = new Properties();

	@Value("${classificator.kbfiles}")
	private String pathToKBFiles;

	@Value("${classificator.metadatafiles}")
	private String metadataFiles;

	@Value("${classificator.regexfiles}")
	private String regexFiles;

	@PostConstruct
	void init() {
		_initServerEnvironment();
	}

	private void _initServerEnvironment() {
		if (System.getProperty("jboss.server.config.dir") != null) {
			_setJBoss();
		} else if (System.getProperty("catalina.base") != null) {
			_setTomcat();
		}

		LOG.debug("detected environment: " + serverProperties.getProperty("serverEnvironment"));
		LOG.debug("set config dir to : " + serverProperties.getProperty("configDir"));
	}

	private void _setTomcat() {
		serverProperties.setProperty("serverEnvironment", "tomcat");
		serverProperties.setProperty("configDir", System.getProperty("catalina.base") + getFileSeperator() + "conf"
				+ getFileSeperator() + "classificator" + getFileSeperator());
	}

	private void _setJBoss() {
		serverProperties.setProperty("serverEnvironment", "jboss");
		serverProperties.setProperty("configDir", System.getProperty("jboss.server.config.dir") + getFileSeperator()
				+ "classificator" + getFileSeperator());
	}

	public String getKnowledgbaseDir() {
		return pathToKBFiles;
	}

	public String getFileSeperator() {
		return System.getProperty("file.separator");
	}
}
