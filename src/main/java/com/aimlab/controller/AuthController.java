package com.aimlab.controller;

import com.aimlab.common.ApiResponse;
import com.aimlab.dto.LoginDto;
import com.aimlab.dto.TokenDto;
import com.aimlab.dto.UserDto;
import com.aimlab.jwt.JwtFilter;
import com.aimlab.jwt.JwtTokenProvider;
import com.aimlab.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationProvider authenticationProvider;

    /**
     * 로그인
     * @param loginDto 이메일, 비밀번호
     */
    @PostMapping("/login")
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginDto.getUserEmail(), loginDto.getUserPassword());

        Authentication authentication = authenticationProvider.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(ApiResponse.success(new TokenDto(jwt)));
    }

    /**
     * 회원가입
     * @param userDto 회원가입 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserDto userDto){
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(userService.signup(userDto)));
    }
}
