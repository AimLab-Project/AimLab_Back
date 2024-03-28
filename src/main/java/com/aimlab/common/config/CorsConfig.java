package com.aimlab.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Order(0)
@RequiredArgsConstructor
@Configuration
public class CorsConfig {
    private final AppProperties appProperties;

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(appProperties.getCors().getAllowedOrigins());
        corsConfiguration.setAllowedMethods(appProperties.getCors().getAllowedMethods());
        corsConfiguration.setAllowedHeaders(appProperties.getCors().getAllowedHeaders());
        corsConfiguration.setExposedHeaders(appProperties.getCors().getAllowedHeaders());

        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

}
