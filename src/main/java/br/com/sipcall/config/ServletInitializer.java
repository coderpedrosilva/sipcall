package br.com.sipcall.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer  extends SpringBootServletInitializer {
	private static final Logger LOGGER  = LoggerFactory.getLogger( ServletInitializer.class );
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	LOGGER.info("ServletInitializer calling Spring Boot Application...");
		return application.sources(br.com.sipcall.SipcallApplication.class);
    }

}
