package com.aimlab.service;

import com.aimlab.common.security.JwtTokenProvider;
import com.aimlab.common.security.UserPrincipal;
import com.aimlab.common.security.oauth.OAuthClientFactory;
import com.aimlab.common.security.oauth.OAuthServerType;
import com.aimlab.dto.authenticate.TokenDto;
import com.aimlab.entity.OAuthUser;
import com.aimlab.repository.OAuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OAuthService {
    private final OAuthClientFactory oAuthClientFactory;
    private final OAuthUserRepository oAuthUserRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenDto authenticate(OAuthServerType serverType, String code){
        // 1. Authenticate code를 통해 OAuth Provider에게서 사용자 정보를 가져옴
        OAuthUser fetchedUser = oAuthClientFactory.fetch(serverType, code);

        // 2. 가져온 사용자가 회원가입되어 있지 않으면 회원가입 시킨다.
        OAuthUser savedUser = oAuthUserRepository.findByOauthId(fetchedUser.getOauthId())
                .orElseGet(() -> oAuthUserRepository.save(fetchedUser));

        // 3. Authentication 객체 생성 후 ThreadLocal에 저장
        Authentication authentication =
                UsernamePasswordAuthenticationToken.authenticated(UserPrincipal.create(savedUser.getUser()), null, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4. JWT Token 발급
        return jwtTokenProvider.createTokens(authentication);
    }
}
