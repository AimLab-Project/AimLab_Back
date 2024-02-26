package com.aimlab.dto.authenticate;

import com.aimlab.dto.RequestDto;
import com.aimlab.dto.ResponseDto;
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
    public static class Request implements RequestDto {
        @NotBlank
        @Email
        @Size(min = 3, max = 50)
        private String userEmail;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
