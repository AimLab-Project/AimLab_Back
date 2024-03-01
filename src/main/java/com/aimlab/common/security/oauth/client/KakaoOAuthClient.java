package com.aimlab.common.security.oauth.client;

import com.aimlab.common.config.AppProperties;
import com.aimlab.common.security.oauth.OAuthServerType;
import com.aimlab.common.security.oauth.model.KakaoTokenDto;
import com.aimlab.common.security.oauth.model.OAuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClient{
    private final HttpExchangeHandler httpExchangeHandler;
    private final AppProperties appProperties;

    @Override
    public OAuthServerType supportServer() {
        return OAuthServerType.KAKAO;
    }

    @Override
    public OAuthUserDto fetch(String code) {
        // 1. OAuth Server로 부터 Access Token을 발급 받는다.
        KakaoTokenDto tokenInfo = httpExchangeHandler.fetchKakaoAccessToken(getParams(code));

        // 2. Access Token을 통해 사용자 정보를 얻는다.
        String accessToken = tokenInfo.getTokenType() + " " + tokenInfo.getAccessToken();

        // 3. 사용자 정보를 얻어 OAuthUser 엔티티를 리턴
        return httpExchangeHandler.fetchKakaoUser(accessToken);
    }

    private MultiValueMap<String, String> getParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", appProperties.getOauth().getKakao().getClientId());
        params.add("client_secret", appProperties.getOauth().getKakao().getClientSecret());
        params.add("redirect_uri", appProperties.getOauth().getKakao().getRedirectUri());
        params.add("code", code);
        return params;
    }
}
