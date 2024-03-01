package com.aimlab.common.security.oauth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenDto {
    private String tokenType;
    private String accessToken;
    private String idToken;
    private Long expiresIn;
    private String refreshToken;
    private String refreshTokenExpiresIn;
    private String scope;
}
