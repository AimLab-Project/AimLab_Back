package com.aimlab.dto.oauth;

import com.aimlab.dto.RequestDto;
import com.aimlab.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class KakaoTokenDto {
    @Getter
    @Setter
    @Builder
    public static class Request implements RequestDto{
        private String grantType;
        private String clientId;
        private String redirectUri;
        private String code;
        private String clientSecret;
    }

    @Getter
    @Setter
    public static class Response implements ResponseDto{
        private String tokenType;
        private String accessToken;
        private String idToken;
        private Long expiresIn;
        private String refreshToken;
        private String refreshTokenExpiresIn;
        private String scope;
    }
}
