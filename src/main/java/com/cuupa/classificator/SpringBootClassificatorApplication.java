package com.cuupa.classificator;

import com.cuupa.classificator.configuration.spring.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = ApplicationConfiguration.class)
public class SpringBootClassificatorApplication extends SpringBootServletInitializer  {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootClassificatorApplication.class, args);
	}
}