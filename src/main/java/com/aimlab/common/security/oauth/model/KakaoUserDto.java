package com.aimlab.common.security.oauth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserDto implements OAuthUserDto{
    private Long id;
    private String email;

    @Override
    public String getOAuthId() {
        return String.valueOf(id);
    }

    @Override
    public String getEmail(){
        return email;
    }
}
