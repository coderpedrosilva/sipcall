package br.com.sipcall.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

  @Value("${sipcall.openapi.dev-url}")
  private String devUrl;

  @Value("${sipcall.openapi.prod-url}")
  private String prodUrl;

  @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in Development environment");

    Server prodServer = new Server();
    prodServer.setUrl(prodUrl);
    prodServer.setDescription("Server URL in Production environment");

    Contact contact = new Contact();
    contact.setEmail("pedro.silva@sipcall.com");
    contact.setName("Pedro");
    contact.setUrl("https://www.sipcall.com");

    License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

    Info info = new Info()
        .title("SIP Call API")
        .version("2.0")
        .contact(contact)
        .description("API de Chamada SIP.").termsOfService("https://www.sipcall.com/terms")
        .license(mitLicense);

    return new OpenAPI().info(info).servers(Arrays.asList(devServer, prodServer));
  }
}