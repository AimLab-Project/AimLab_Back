package com.aimlab.util;

import com.aimlab.common.config.AppProperties;
import com.aimlab.common.security.oauth.OAuthServerType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CookieUtil {
    private final AppProperties appProperties;

    private static String credentialCookieDomain;

    private static String clientCookieDomain;

    /**
     * 빈 초기화 : static 필드 값 주입
     */
    @PostConstruct
    public void init() {
        credentialCookieDomain = appProperties.getCors().getCredentialCookieDomain();
        clientCookieDomain = appProperties.getCors().getClientCookieDomain();
    }

    /**
     * Refresh Token Cookie 생성
     * @param refreshToken 리프레시 토큰
     */
    public static ResponseCookie getRefreshTokenCookie(String refreshToken){
        return ResponseCookie
                .from("refresh_token")
                .value(refreshToken)
                .domain(credentialCookieDomain)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("none")
                .maxAge(60*60*24*30)    // 30일
                .build();
    }

    /**
     * 마지막 로그인 유형 Cookie 생성
     * @param serverType 서버 타입
     */
    public static ResponseCookie getLastLoginMethodCookie(OAuthServerType serverType){
        return ResponseCookie
                .from("last_login_method")
                .value(serverType.toString().toLowerCase())
                .domain(clientCookieDomain)
                .path("/")
                .httpOnly(false)
                .secure(true)
                .sameSite("none")
                .maxAge(60*60*24*365)   // 1년
                .build();
    }
}
