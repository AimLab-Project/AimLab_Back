package com.aimlab.dto;

import com.aimlab.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotNull
    @Size(min = 3, max = 50)
    private String userEmail;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String userPassword;

    @NotNull
    @Size(min = 3, max = 50)
    private String userNickname;

    public static UserDto fromEntity(User user){
        if(user == null)
            return null;

        return UserDto.builder()
                .userEmail(user.getUserEmail())
                .userNickname(user.getUserNickname()).build();
    }
}
