package com.aimlab.dto.auth;

import com.aimlab.dto.RequestDto;
import com.aimlab.dto.ResponseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class EmailVerificationDto {
    @Getter
    @Setter
    public static class Request implements RequestDto {
        @Email
        @NotBlank
        private String userEmail;
    }

    @Getter
    @Setter
    @Builder
    public static class Response implements ResponseDto {
        private String key;
    }
}
