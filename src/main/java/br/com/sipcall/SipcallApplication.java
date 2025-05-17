package br.com.sipcall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.sipcall")
public class SipcallApplication {
	
	private static final Logger LOGGER  = LoggerFactory.getLogger( SipcallApplication.class );
	
	public static void main(String[] args) {
		LOGGER.info("Starting Spring Boot application...");
		SpringApplication.run(SipcallApplication.class, args);
		LOGGER.info("Started Spring Boot application...");
		
	}

}
