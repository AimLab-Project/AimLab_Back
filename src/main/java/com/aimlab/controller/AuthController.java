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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "2. 인증/인가")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MailVerificationService mailVerificationService;

    /**
     * 로그인
     * @param request 이메일, 비밀번호
     */
    @Operation(summary = "일반 로그인", description = "일반 회원가입 유저의 로그인 엔드포인트")
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginDto.Response>> authorize(
            @Valid @RequestBody LoginDto.Request request){

        TokenDto tokenDto = authService.login(request.getUserEmail(), request.getUserPassword());

        UserPrincipal user = SecurityUtil.getCurrentUser().orElseThrow();

        // Refresh Token Cookie (보관 기간 : 30일)
        ResponseCookie refreshTokenCookie = CookieUtil.getNewCookie("refresh_token", tokenDto.getRefreshToken(), 60*60*24*30);

        // 마지막 로그인 방법 (보관 기간 : 356일, not http only)
        ResponseCookie lastLoginMethodCookie = CookieUtil.getNewCookie("last_login_method", "", 60*60*24*365, false);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, lastLoginMethodCookie.toString())
                .body(ResponseUtil.success(LoginDto.Response.builder()
                        .userId(user.getUserId())
                        .userEmail(user.getUserEmail())
                        .userNickname(user.getUserNickname())
                        .accessToken(tokenDto.getAccessToken()).build()));
    }
    /**
     * 회원가입
     */
    @Operation(summary = "일반 회원가입", description = "일반 회원가입 엔드포인트")
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
    @Operation(summary = "Token 재발급", description = "Access Token, Refresh Token 재발급 엔드포인트")
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
    @Operation(summary = "이메일 인증번호 발급", description = "인증번호를 이메일로 발급하는 엔드포인트")
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
    @Operation(summary = "인증번호 확인", description = "이메일에서 확인한 인증번호를 확인하는 엔드포인트")
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
