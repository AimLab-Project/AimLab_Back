package com.aimlab.controller;

import com.aimlab.common.ApiResponse;
import com.aimlab.common.ErrorCode;
import com.aimlab.dto.*;
import com.aimlab.exception.CustomException;
import com.aimlab.security.UserPrincipal;
import com.aimlab.service.AuthService;
import com.aimlab.service.MailVerificationService;
import com.aimlab.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MailVerificationService mailVerificationService;

    /**
     * 로그인
     * @param request 이메일, 비밀번호
     */
    @PostMapping("/login")
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginDto.Request request){
        TokenDto tokenDto = authService.login(request.getUserEmail(), request.getUserPassword());

        UserPrincipal user = SecurityUtil.getCurrentUser().orElseThrow();

        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/").build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(LoginDto.Response.builder()
                        .userId(user.getUserId())
                        .userEmail(user.getUserEmail())
                        .userNickname(user.getUserNickname())
                        .accessToken(tokenDto.getAccessToken()).build()));
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
                        .userEmail(request.getUserEmail())
                        .userNickname(request.getUserNickname()).build()));
    }

    /**
     * Refresh Token 재발급 <br>
     * Access Token은 Response Body, Refresh Token은 Http Only Cookie로 보낸다.
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(name = "refresh_token", required = true) String refreshToken ){
        if(refreshToken == null || refreshToken.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        TokenDto tokenDto = authService.refreshTokens(refreshToken);

        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/").build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(
                        TokenDto.Response.builder()
                                .accessToken(tokenDto.getAccessToken()).build()));
    }

    /**
     * 이메일 인증 코드 발급
     * @param request 이메일
     */
    @PostMapping("/email/verification")
    public ResponseEntity<?> sendEmailVerificationCode(@Valid @RequestBody EmailVerificationDto.Request request){
        String key = mailVerificationService.createVerification(request.getUserEmail());

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(
                        EmailVerificationDto.Response.builder().key(key).build()));
    }

    /**
     * 이메일 인증 코드 확인
     * @param request 키, 이메일, 인증 코드
     */
    @PostMapping("/email/verification/confirm")
    public ResponseEntity<?> verificateCode(@Valid @RequestBody EmailVerificationConfirmDto.Request request){
        mailVerificationService.confirmVerification(
                request.getKey(),
                request.getUserEmail(),
                request.getVerificationCode());

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(EmailVerificationConfirmDto.Response.builder()
                        .key(request.getKey()).build()));
    }
}
