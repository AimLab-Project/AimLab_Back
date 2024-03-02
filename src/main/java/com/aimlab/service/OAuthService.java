package com.aimlab.service;

import com.aimlab.common.security.JwtTokenProvider;
import com.aimlab.common.security.UserPrincipal;
import com.aimlab.common.security.oauth.client.OAuthClientFactory;
import com.aimlab.common.security.oauth.OAuthServerType;
import com.aimlab.common.security.oauth.model.OAuthUserDto;
import com.aimlab.dto.authenticate.TokenDto;
import com.aimlab.entity.Authority;
import com.aimlab.entity.OAuthUser;
import com.aimlab.entity.User;
import com.aimlab.repository.OAuthUserRepository;
import com.aimlab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class OAuthService {
    private final OAuthClientFactory oAuthClientFactory;
    private final OAuthUserRepository oAuthUserRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenDto authenticate(OAuthServerType serverType, String code){
        LocalDateTime currentTime = LocalDateTime.now();    // 현재 시간

        // 1. Authenticate code를 통해 OAuth Provider에게서 사용자 정보를 가져옴
        OAuthUserDto fetchedSocialUserInfo = oAuthClientFactory.fetch(serverType, code);

        // 2. 가져온 사용자가 회원가입되어 있지 않으면 회원가입 시킨다.
        Optional<OAuthUser> socialUser = oAuthUserRepository.findByOauthIdAndOauthServerType(fetchedSocialUserInfo.getOAuthId(), serverType);
        User user;
        if(socialUser.isEmpty()){
            user = userRepository.findOneByUserEmail(fetchedSocialUserInfo.getEmail())
                    .orElseGet(() -> userRepository.save(User.builder()
                            .userEmail(fetchedSocialUserInfo.getEmail())
                            .userNickname("GUEST" + fetchedSocialUserInfo.getOAuthId())
                            .authority(Authority.ROLE_USER)
                            .createdAt(currentTime)
                            .modifiedAt(currentTime).build()));

            oAuthUserRepository.save(OAuthUser.builder()
                    .oauthId(fetchedSocialUserInfo.getOAuthId())
                    .oauthServerType(serverType)
                    .createdAt(currentTime)
                    .user(user).build());
        } else {
            user = socialUser.get().getUser();
        }

        // 3. Authentication 객체 생성 후 ThreadLocal에 저장
        Authentication authentication =
                UsernamePasswordAuthenticationToken.authenticated(UserPrincipal.create(user), null, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4. JWT Token 발급
        return jwtTokenProvider.createTokens(authentication);
    }

    public String getLoginRedirectUri(OAuthServerType serverType){
        return oAuthClientFactory.getLoginRedirectUri(serverType);
    }
}
