package com.aimlab.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class EmailVerificationConfirmDto {
    @Getter
    @Setter
    public static class Request implements RequestDto{
        @NotBlank
        private String key;

        @Email
        @NotBlank
        private String user_email;

        @NotBlank
        private String verification_code;
    }

    @Getter
    @Setter
    @Builder
    public static class Response implements ResponseDto{
        private String key;
    }
}
