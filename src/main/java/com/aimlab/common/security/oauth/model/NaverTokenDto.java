package com.aimlab.common.security.oauth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverTokenDto{
    private String tokenType;
    private String accessToken;
    private Long expiresIn;
    private String refreshToken;
    private String scope;
}
