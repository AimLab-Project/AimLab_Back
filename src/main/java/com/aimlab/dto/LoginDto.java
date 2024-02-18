package com.aimlab.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

public class LoginDto {

    @Getter
    @Setter
    public static class Request implements RequestDto{
        @NotBlank
        @Email
        @Size(min = 3, max = 50)
        private String user_email;

        @NotBlank
        @Size(min = 3, max = 100)
        private String user_password;
    }

    @Getter
    @Setter
    @Builder
    public static class Response implements ResponseDto{
        private String user_id;
        private String user_email;
        private String user_nickname;
        private String accessToken;
    }
}
