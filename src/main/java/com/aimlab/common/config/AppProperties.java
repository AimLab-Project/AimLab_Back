package com.aimlab.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final Mail mail = new Mail();
    private final Oauth oauth = new Oauth();
    private final Cors cors = new Cors();

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
        private long maxAuthenticationAttempts;
    }

    @Getter
    @Setter
    public static class Oauth{
        private final Provider kakao = new Provider();
        private final Provider naver = new Provider();
        private final Provider google = new Provider();

        @Getter
        @Setter
        public static class Provider{
            private String clientId;
            private String clientSecret;
            private String redirectUri;
            private String Scope;
            private String state;
        }
    }

    @Getter
    @Setter
    public static class Cors{
        private List<String> allowedOrigins;
        private List<String> allowedMethods;
        private List<String> allowedHeaders;
    }
}
