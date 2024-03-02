package com.aimlab.controller;

import com.aimlab.common.security.UserPrincipal;
import com.aimlab.dto.authenticate.LoginDto;
import com.aimlab.dto.authenticate.TokenDto;
import com.aimlab.dto.oauth.OAuthLoginDto;
import com.aimlab.common.security.oauth.OAuthServerType;
import com.aimlab.service.OAuthService;
import com.aimlab.util.CookieUtil;
import com.aimlab.util.SecurityUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("/{oauthServerType}")
    public ResponseEntity<?> sendRedirectToOAuthLoginPage(@PathVariable OAuthServerType oauthServerType, HttpServletResponse response) throws IOException {
        response.sendRedirect(oAuthService.getLoginRedirectUri(oauthServerType));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login/{oauthServerType}")
    public ResponseEntity<?> oauthLogin(@PathVariable OAuthServerType oauthServerType,
                                        @RequestBody OAuthLoginDto.Request request){
        TokenDto tokenDto = oAuthService.authenticate(oauthServerType, request.getCode());

        UserPrincipal user = SecurityUtil.getCurrentUser().orElseThrow();

        ResponseCookie cookie = CookieUtil.getNewCookie("refresh_token", tokenDto.getRefreshToken(), 60*60*24*30);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(LoginDto.Response.builder()
                        .userId(user.getUserId())
                        .userNickname(user.getUserNickname())
                        .userEmail(user.getUserEmail())
                        .accessToken(tokenDto.getAccessToken()).build());
    }
}
