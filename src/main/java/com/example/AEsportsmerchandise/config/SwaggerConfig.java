package com.example.AEsportsmerchandise.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("AESports")
                        .description(
                                "AESports is an online gaming merchandise platform built for purchasing and managing " +
                                        "high-quality esports products such as jerseys, finger sleeves, gaming chairs, and accessories.\n\n" +
                                        "The platform provides secure user authentication, role-based access control, product catalog " +
                                        "management, cart and order processing, payment handling, and user reviews through RESTful APIs.\n\n" +
                                        "This API is designed for seamless frontend integration, admin operations, and scalable " +
                                        "deployment in a production-ready e-commerce environment."
                        )
                        .version("1.0")
                )
                // 🔐 JWT Security
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new io.swagger.v3.oas.models.Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
