package com.aimlab.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Aimsharp 백엔드 API 문서",
                description = "API 명세서",
                contact = @Contact(
                        name = "Aimsharp",
                        email = "aimsharp12@gmail.com"
                )
        )
)
@Configuration
public class OpenApiConfig {
}
