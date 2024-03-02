package com.aimlab.common.security.oauth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverUserDto implements OAuthUserDto{
    private String resultCode;
    private String message;
    private Response response;

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response{
        private String id;
        private String email;
        private String nickname;
    }

    @Override
    public String getOAuthId() {
        return response.id;
    }

    @Override
    public String getEmail() {
        return response.email;
    }
}
