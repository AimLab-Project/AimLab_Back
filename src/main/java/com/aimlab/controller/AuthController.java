package com.aimlab.controller;

import com.aimlab.common.ApiResponse;
import com.aimlab.common.ErrorCode;
import com.aimlab.dto.*;
import com.aimlab.exception.CustomException;
import com.aimlab.security.JwtAuthenticationFilter;
import com.aimlab.service.AuthService;
import com.aimlab.service.MailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MailVerificationService mailVerificationService;

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
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpDto.Request request){
        authService.signup(request);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(SignUpDto.Response.builder()
                        .user_email(request.getUser_email())
                        .user_nickname(request.getUser_nickname()).build()));
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

    /**
     * 이메일 인증 코드 발급
     * @param emailVerificationDto 이메일
     */
    @PostMapping("/email/verification")
    public ResponseEntity<?> sendEmailVerificationCode(@Valid @RequestBody EmailVerificationDto emailVerificationDto){
        String key = mailVerificationService.createVerification(emailVerificationDto.getUser_email());

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(key));
    }

    /**
     * 이메일 인증 코드 확인
     * @param emailVerificationDto 키, 이메일, 인증 코드
     */
    @GetMapping("/email/verification/confirm")
    public ResponseEntity<?> verificateCode(@Valid @RequestBody EmailVerificationDto emailVerificationDto){
        mailVerificationService.confirmVerification(emailVerificationDto);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success("이메일 인증이 완료되었습니다."));
    }
}
