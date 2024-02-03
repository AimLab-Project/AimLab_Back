package com.aimlab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * JWT 토큰 DTO
 * Token 타입은 Bearer
 */
@Getter
@Setter
@Builder
public class TokenDto {
    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}
