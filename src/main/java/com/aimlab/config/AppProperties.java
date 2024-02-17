package com.aimlab.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final Mail mail = new Mail();

    @Getter
    @Setter
    public static class Auth{
        private String header;
        private String secret;
        private long accessTokenValidity;
        private long refreshTokenValidity;
    }

    @Getter
    @Setter
    public static class Mail{
        private long verificationValidityTime;
        private long verificationRetentionTime;
    }
}
