package com.aimlab.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class EmailVerificationDto {
    @Getter
    @Setter
    public static class Request implements RequestDto{
        @Email
        @NotBlank
        private String user_email;
    }

    @Getter
    @Setter
    @Builder
    public static class Response implements ResponseDto{
        private String key;
    }
}
