package com.aimlab.dto.oauth;

import com.aimlab.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;

public class KakaoUserDto {
    @Getter
    @Setter
    public static class Response implements ResponseDto{
        private Long id;
        private String email;
    }
}
