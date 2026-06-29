package it.mixer.demo.warehouse.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(ObjectProvider<BuildProperties> buildProperties) {
        String version = buildProperties.getIfAvailable() != null 
                ? buildProperties.getIfAvailable().getVersion() 
                : "dev";

        return new OpenAPI()
                .info(new Info()
                        .title("Warehouse Management API")
                        .version(version)
                        .description("Reactive REST API for managing warehouse inventory.")
                        .contact(new Contact().name("API Support").email("ict@mixer.it"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
