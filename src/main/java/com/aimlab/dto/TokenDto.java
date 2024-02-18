package com.aimlab.dto;

import lombok.*;

/**
 * JWT 토큰 DTO
 * Token 타입은 Bearer
 */
@Getter
@Setter
@Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    @Getter
    @Setter
    @Builder
    public static class Response implements ResponseDto{
        private String access_token;
    }
}
