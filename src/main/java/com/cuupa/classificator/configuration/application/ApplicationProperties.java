package com.cuupa.classificator.configuration.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ApplicationProperties {

	private static final Log LOG = LogFactory.getLog(ApplicationProperties.class);

	private static final Properties properties = new Properties();

	private static final Properties serverProperties = new Properties();

	static {
		_initServerEnvironment();
		_readKBFiles();
	}

	private static void _readKBFiles() {
		File file = new File(serverProperties.getProperty("configDir") + "application.properties");

		try (InputStream in = new FileInputStream(file)) {
			properties.load(in);
			new File(getKnowledgbaseDir()).mkdirs();
		} catch (IOException e) {
			LOG.error("couldn't read config file", e);
		}
	}

	private static void _initServerEnvironment() {
		if (System.getProperty("jboss.server.config.dir") != null) {
			_setJBoss();
		} else if (System.getProperty("catalina.base") != null) {
			_setTomcat();
		}

		LOG.debug("detected environment: " + serverProperties.getProperty("serverEnvironment"));
		LOG.debug("set config dir to : " + serverProperties.getProperty("configDir"));
	}

	private static void _setTomcat() {
		serverProperties.setProperty("serverEnvironment", "tomcat");
		serverProperties.setProperty("configDir", System.getProperty("catalina.base") + getFileSeperator() + "conf"
				+ getFileSeperator() + "mailprocessor/classificator" + getFileSeperator());
	}

	private static void _setJBoss() {
		serverProperties.setProperty("serverEnvironment", "jboss");
		serverProperties.setProperty("configDir", System.getProperty("jboss.server.config.dir") + getFileSeperator()
				+ "classificator" + getFileSeperator());
	}

	public static String getKnowledgbaseDir() {
		return properties.getProperty("knowledgebase", "");
	}

	public static String getFileSeperator() {
		return System.getProperty("file.separator");
	}
}
