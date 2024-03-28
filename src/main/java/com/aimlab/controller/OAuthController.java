package com.aimlab.controller;

import com.aimlab.common.security.UserPrincipal;
import com.aimlab.dto.SuccessResponse;
import com.aimlab.dto.auth.LoginDto;
import com.aimlab.dto.auth.TokenDto;
import com.aimlab.dto.oauth.OAuthLoginDto;
import com.aimlab.common.security.oauth.OAuthServerType;
import com.aimlab.service.OAuthService;
import com.aimlab.util.CookieUtil;
import com.aimlab.util.ResponseUtil;
import com.aimlab.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "3. 소셜 로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class OAuthController {
    private final OAuthService oAuthService;

    /**
     * 소셜 로그인 화면 redirect 요청
     */
    @Operation(summary = "소셜 로그인 redirect uri 요청", description = "각 소셜로그인 서비스의 로그인 화면으로 이동하기 위한 redirect uri 제공 엔드포인트")
    @GetMapping("/{oauthServerType}")
    public ResponseEntity<?> sendRedirectToOAuthLoginPage(
            @PathVariable OAuthServerType oauthServerType,
            HttpServletResponse response) throws IOException {

        response.sendRedirect(oAuthService.getLoginRedirectUri(oauthServerType));
        return ResponseEntity.ok().build();
    }

    /**
     * 소셜 로그인 요청 (인가 코드 전달)
     */
    @Operation(summary = "소셜 로그인", description = "소셜 로그인 엔드포인트(인가 코드, 소셜 타입 필요)")
    @PostMapping("/login/{oauthServerType}")
    public ResponseEntity<SuccessResponse<LoginDto.Response>> oauthLogin(
            @PathVariable OAuthServerType oauthServerType,
            @RequestBody OAuthLoginDto.Request request){

        TokenDto tokenDto = oAuthService.authenticate(oauthServerType, request.getCode());

        UserPrincipal user = SecurityUtil.getCurrentUser().orElseThrow();

        // Refresh Token 쿠키
        ResponseCookie refreshTokenCookie = CookieUtil.getRefreshTokenCookie(tokenDto.getRefreshToken());

        // 마지막 로그인 방법 (보관 기간 : 356일, not http only)
        ResponseCookie lastLoginMethodCookie = CookieUtil.getLastLoginMethodCookie(oauthServerType);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, lastLoginMethodCookie.toString())
                .body(ResponseUtil.success(
                        LoginDto.Response.builder()
                        .userId(user.getUserId())
                        .userNickname(user.getUserNickname())
                        .userEmail(user.getUserEmail())
                        .accessToken(tokenDto.getAccessToken()).build()));
    }
}
