package com.aimlab.controller;

import com.aimlab.common.ApiResponse;
import com.aimlab.common.ErrorCode;
import com.aimlab.dto.LoginDto;
import com.aimlab.dto.TokenDto;
import com.aimlab.dto.UserDto;
import com.aimlab.exception.CustomException;
import com.aimlab.security.JwtAuthenticationFilter;
import com.aimlab.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 로그인
     * @param loginDto 이메일, 비밀번호
     */
    @PostMapping("/login")
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginDto loginDto){
        TokenDto jwt = authService.login(loginDto.getUserEmail(), loginDto.getUserPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt.getAccessToken());

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(ApiResponse.success(jwt));
    }

    /**
     * 회원가입
     * @param userDto 회원가입 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserDto userDto){
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(authService.signup(userDto)));
    }

    /**
     * Refresh Token 재발급
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(name = "refresh_token", required = false) String refreshToken ){
        if(refreshToken == null || refreshToken.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(authService.refreshTokens(refreshToken)));
    }
}
