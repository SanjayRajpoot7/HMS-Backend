package com.example.apigatewayservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // Add this!

@Configuration // Missing annotation: Required for Spring to detect this as a config class
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info() // No need for full package path (io.swagger.v3...)
                        .title("Hotel API")
                        .version("1.0")
                        .description("API documentation for Hotel Management System")
                        .contact(new Contact() // Optional: Add contact info
                                .name("API Support")
                                .email("support@hotel.com"))
                        .license(new License() // Optional: Add license
                                .name("Apache 2.0")));
    }

    @Bean
    public GroupedOpenApi roomsApi() {
        return GroupedOpenApi.builder()
                .group("rooms")
                .pathsToMatch("/api/rooms/**")
                .build();
    }

    @Bean
    public GroupedOpenApi bookingsApi() {
        return GroupedOpenApi.builder()
                .group("bookings")
                .pathsToMatch("/api/bookings/**")
                .build();
    }
}