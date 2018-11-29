package com.cuupa.classificator;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.cuupa.classificator.configuration.spring.ApplicationConfiguration;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = ApplicationConfiguration.class)
public class SpringBootClassificatorApplication extends SpringBootServletInitializer  {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootClassificatorApplication.class, args);
	}
}