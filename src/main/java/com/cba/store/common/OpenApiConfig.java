package com.cba.store.common;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
   @Value("${websiteUrl}")
   private String websiteUrl;
    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl(websiteUrl);
        return new OpenAPI()
                .info(new Info().title("Swagger Auth Token Store API")
                        .description("Store application REST API with centralized token authentication using Swagger @SecurityRequirement.")
                        .version("1.0.0"))
                .servers(List.of(server))
                .addSecurityItem(new SecurityRequirement().addList("authorization"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("authorization",
                                new SecurityScheme()
                                        .name("authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .description("JWT auth description")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }
}
