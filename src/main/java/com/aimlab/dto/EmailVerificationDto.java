package com.aimlab.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailVerificationDto {
    private String key;
    private String user_email;
    private String verification_code;
}
