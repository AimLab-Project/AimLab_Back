package com.aimlab.dto.auth;

import com.aimlab.dto.RequestDto;
import com.aimlab.dto.ResponseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

public class LoginDto {

    @Getter
    @Setter
    public static class Request implements RequestDto {
        @NotBlank
        @Email
        @Size(min = 3, max = 50)
        private String userEmail;

        @NotBlank
        @Size(min = 3, max = 100)
        private String userPassword;
    }

    @Getter
    @Setter
    @Builder
    public static class Response implements ResponseDto {
        private String userId;
        private String userEmail;
        private String userNickname;
        private String accessToken;
    }
}
