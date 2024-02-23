package com.aimlab.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomAuthenticateProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    /**
     * 로그인 유효성 검사(이메일, 비밀번호 확인)
     * @param authentication the authentication request object.
     * @return Authentication 객체
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String)authentication.getCredentials();

        UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(email);
        if(!passwordEncoder.matches(password, userPrincipal.getPassword())){
            throw new BadCredentialsException("잘못된 비밀번호 입니다");
        }

        return UsernamePasswordAuthenticationToken.authenticated(userPrincipal, null, userPrincipal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
