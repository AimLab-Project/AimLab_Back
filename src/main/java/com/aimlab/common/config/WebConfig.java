package com.aimlab.common.config;

import com.aimlab.common.security.oauth.client.HttpExchangeHandler;
import com.aimlab.common.security.oauth.OAuthServerTypeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public HttpExchangeHandler kakaoHttpClient(){
        RestClient restClient = RestClient.create();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient)).build();

        return factory.createClient(HttpExchangeHandler.class);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OAuthServerTypeConverter());
    }
}
