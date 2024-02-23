package com.aimlab.dto.authenticate;

import com.aimlab.dto.RequestDto;
import com.aimlab.dto.ResponseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class EmailVerificationConfirmDto {
    @Getter
    @Setter
    public static class Request implements RequestDto {
        @NotBlank
        private String key;

        @Email
        @NotBlank
        private String userEmail;

        @NotBlank
        private String verificationCode;
    }

    @Getter
    @Setter
    @Builder
    public static class Response implements ResponseDto {
        private String key;
    }
}
