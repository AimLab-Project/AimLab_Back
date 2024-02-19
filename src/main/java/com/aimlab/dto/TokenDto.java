package com.aimlab.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

/**
 * JWT 토큰 DTO
 * Token 타입은 Bearer
 */
@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    @Getter
    @Setter
    @Builder
    public static class Response implements ResponseDto{
        private String accessToken;
    }
}
