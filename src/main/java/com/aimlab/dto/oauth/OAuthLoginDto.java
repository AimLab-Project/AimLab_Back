package com.aimlab.dto.oauth;

import com.aimlab.dto.RequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class OAuthLoginDto {
    @Getter
    @Setter
    public static class Request implements RequestDto{
        @NotBlank
        private String code;
    }
}
