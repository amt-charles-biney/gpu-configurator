package com.amalitech.gpuconfigurator.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(contact = @Contact(name = "S3vers"),
                description = "OpenAPI Documentation for GPU configurator",
                title = "GPU Configurator", version = "1.0.0")
)
@SecurityScheme(name = "bearerAuth", description = "JWT auth description",
        scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
@Configuration
public class OpenApiConfig {
}
