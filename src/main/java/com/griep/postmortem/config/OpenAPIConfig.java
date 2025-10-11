package com.griep.postmortem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(getInfo()
                        .contact(getContact())
                        .license(getLicense()));
    }

    private static Info getInfo() {
        return new Info()
                .title("Postmortem")
                .version("1.0")
                .description("API de Postmortem")
                .termsOfService("http://example.com/terms/");
    }

    private static License getLicense() {
        return new License()
                .name("Apache 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0.html");
    }

    private static Contact getContact() {
        return new Contact()
                .name("Felipe Griep")
                .email("griep.felipe@gmail.com")
                .url("http://example.com/contact");
    }
}
