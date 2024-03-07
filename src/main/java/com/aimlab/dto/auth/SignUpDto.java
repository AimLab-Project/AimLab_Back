package com.aimlab.dto.auth;

import com.aimlab.dto.RequestDto;
import com.aimlab.dto.ResponseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class SignUpDto {

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

        @NotBlank
        @Size(min = 3, max = 50)
        private String userNickname;

        @NotBlank
        private String key;
    }

    @Getter
    @Setter
    @Builder
    public static class Response implements ResponseDto {
        private String userEmail;
        private String userNickname;
        private String accessToken;
    }
}
