package com.aimlab.common.security.oauth;

import com.aimlab.common.config.AppProperties;
import com.aimlab.dto.oauth.KakaoTokenDto;
import com.aimlab.dto.oauth.KakaoUserDto;
import com.aimlab.entity.Authority;
import com.aimlab.entity.OAuthUser;
import com.aimlab.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClient{
    private final KakaoHttpClient kakaoHttpClient;
    private final AppProperties appProperties;

    @Override
    public OAuthServerType supportServer() {
        return OAuthServerType.KAKAO;
    }

    @Override
    public OAuthUser fetch(String code) {
        // 1. OAuth Server로 부터 Access Token을 발급 받는다.
        KakaoTokenDto.Response tokenInfo = kakaoHttpClient.fetchAccessToken(getParams(code));

        // 2. Access Token을 통해 사용자 정보를 얻는다.
        String accessToken = tokenInfo.getTokenType() + " " + tokenInfo.getAccessToken();
        KakaoUserDto.Response userDto = kakaoHttpClient.fetchUser(accessToken);

        // 3. 사용자 정보를 얻어 OAuthUser 엔티티를 리턴
        return OAuthUser.builder()
                .oauthId(String.valueOf(userDto.getId()))
                .email(userDto.getEmail())
                .oAuthServerType(OAuthServerType.KAKAO)
                .user(User.builder()
                        .userNickname("GUEST" + userDto.getId())
                        .userEmail(userDto.getEmail())
                        .authority(Authority.ROLE_USER)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now()).build()).build();
    }

    private MultiValueMap<String, String> getParams(String code) {
        AppProperties.Oauth.Kakao kakaoProperties = appProperties.getOauth().getKakao();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("client_secret", kakaoProperties.getClientSecret());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);
        return params;
    }
}
