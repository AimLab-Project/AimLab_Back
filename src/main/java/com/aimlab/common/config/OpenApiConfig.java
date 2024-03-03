package com.aimlab.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
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
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE);
        return mapper;
    }

    @Bean
    public ModelResolver modelResolver(){
        return new ModelResolver(objectMapper());
    }
}
