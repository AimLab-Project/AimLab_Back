package com.aimlab.common.security.oauth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleTokenDto {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private String scope;
    private Long expiresIn;
}
