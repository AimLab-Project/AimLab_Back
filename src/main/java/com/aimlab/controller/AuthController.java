package com.aimlab.controller;

import com.aimlab.common.exception.ErrorCode;
import com.aimlab.common.exception.CustomException;
import com.aimlab.common.security.UserPrincipal;
import com.aimlab.dto.SuccessResponse;
import com.aimlab.dto.auth.*;
import com.aimlab.service.AuthService;
import com.aimlab.service.MailVerificationService;
import com.aimlab.util.CookieUtil;
import com.aimlab.util.ResponseUtil;
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
    public ResponseEntity<SuccessResponse<LoginDto.Response>> authorize(
            @Valid @RequestBody LoginDto.Request request){

        TokenDto tokenDto = authService.login(request.getUserEmail(), request.getUserPassword());

        UserPrincipal user = SecurityUtil.getCurrentUser().orElseThrow();

        ResponseCookie cookie = CookieUtil.getNewCookie("refresh_token", tokenDto.getRefreshToken(), 60*60*24*30);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ResponseUtil.success(LoginDto.Response.builder()
                        .userId(user.getUserId())
                        .userEmail(user.getUserEmail())
                        .userNickname(user.getUserNickname())
                        .accessToken(tokenDto.getAccessToken()).build()));
    }
    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<SignUpDto.Response>> signup(
            @Valid @RequestBody SignUpDto.Request request){

        TokenDto tokenDto = authService.signup(request);

        ResponseCookie cookie = CookieUtil.getNewCookie("refresh_token", tokenDto.getRefreshToken(), 60*60*24*30);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ResponseUtil.success(SignUpDto.Response.builder()
                        .userEmail(request.getUserEmail())
                        .userNickname(request.getUserNickname())
                        .accessToken(tokenDto.getAccessToken()).build()));
    }

    /**
     * Refresh Token 재발급 <br>
     * Access Token은 Response Body, Refresh Token은 Http Only Cookie로 보낸다.
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<SuccessResponse<TokenDto.Response>> refreshAccessToken(
            @CookieValue(name = "refresh_token", required = true) String refreshToken ){

        if(refreshToken == null || refreshToken.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        TokenDto tokenDto = authService.refreshTokens(refreshToken);

        ResponseCookie cookie = CookieUtil.getNewCookie("refresh_token", tokenDto.getRefreshToken(), 60*60*24*30);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ResponseUtil.success(
                        TokenDto.Response.builder()
                                .accessToken(tokenDto.getAccessToken()).build()));
    }

    /**
     * 이메일 인증 코드 발급
     * @param request 이메일
     */
    @PostMapping("/email/verification")
    public ResponseEntity<SuccessResponse<EmailVerificationDto.Response>> sendEmailVerificationCode(
            @Valid @RequestBody EmailVerificationDto.Request request){

        String key = mailVerificationService.createVerification(request.getUserEmail());

        return ResponseEntity
                .ok()
                .body(ResponseUtil.success(
                        EmailVerificationDto.Response.builder().key(key).build()));
    }

    /**
     * 이메일 인증 코드 확인
     * @param request 키, 이메일, 인증 코드
     */
    @PostMapping("/email/verification/confirm")
    public ResponseEntity<SuccessResponse<EmailVerificationConfirmDto.Response>> verificateCode(
            @Valid @RequestBody EmailVerificationConfirmDto.Request request){

        mailVerificationService.confirmVerification(
                request.getKey(),
                request.getUserEmail(),
                request.getVerificationCode());

        return ResponseEntity
                .ok()
                .body(ResponseUtil.success(EmailVerificationConfirmDto.Response.builder()
                        .key(request.getKey()).build()));
    }
}
