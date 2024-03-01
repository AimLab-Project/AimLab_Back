package com.aimlab.common.security.oauth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleUserDto implements OAuthUserDto{
    private String id;
    private String email;
    private boolean verified_email;
    private String picture;

    @Override
    public String getOAuthId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
