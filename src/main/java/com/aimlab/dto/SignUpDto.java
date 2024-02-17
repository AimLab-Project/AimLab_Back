package com.aimlab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class SignUpDto {

    @Getter
    @Setter
    public static class Request{
        @NotBlank
        @Email
        @Size(min = 3, max = 50)
        private String user_email;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank
        @Size(min = 3, max = 100)
        private String user_password;

        @NotBlank
        @Size(min = 3, max = 50)
        private String user_nickname;

        @NotBlank
        private String key;
    }

    @Getter
    @Setter
    @Builder
    public static class Response{
        private String user_email;
        private String user_nickname;
    }
}
