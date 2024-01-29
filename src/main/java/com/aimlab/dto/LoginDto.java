package com.aimlab.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotBlank
    @Email
    @Size(min = 3, max = 50)
    private String userEmail;

    @NotBlank
    @Size(min = 3, max = 100)
    private String userPassword;
}
