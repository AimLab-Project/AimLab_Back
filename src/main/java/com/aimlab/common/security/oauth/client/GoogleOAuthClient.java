package com.aimlab.common.security.oauth.client;

import com.aimlab.common.config.AppProperties;
import com.aimlab.common.security.oauth.OAuthServerType;
import com.aimlab.common.security.oauth.model.GoogleTokenDto;
import com.aimlab.common.security.oauth.model.OAuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GoogleOAuthClient implements OAuthClient{
    private final HttpExchangeHandler httpExchangeHandler;
    private final AppProperties appProperties;

    @Override
    public OAuthServerType supportServer() {
        return OAuthServerType.GOOGLE;
    }

    @Override
    public String getLoginRedirectUri() {
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", appProperties.getOauth().getGoogle().getClientId())
                .queryParam("redirect_uri", appProperties.getOauth().getGoogle().getRedirectUri())
                .queryParam("scope", appProperties.getOauth().getGoogle().getScope())
                .queryParam("response_type", "code").toUriString();
    }

    @Override
    public OAuthUserDto fetch(String code) {
        // 1. OAuth Server로 부터 Access Token을 발급 받는다.
        GoogleTokenDto tokenDto = httpExchangeHandler.fetchGoogleAccessToken(getParam(code));

        // 2. Access Token을 통해 사용자 정보를 얻는다.
        String accessToken = tokenDto.getTokenType() + " " + tokenDto.getAccessToken();

        // 3. 사용자 정보를 얻어 OAuthUser 엔티티를 리턴
        return httpExchangeHandler.fetchGoogleUser(accessToken, tokenDto.getAccessToken());
    }

    private MultiValueMap<String, String> getParam(String code){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", appProperties.getOauth().getGoogle().getClientId());
        params.add("client_secret", appProperties.getOauth().getGoogle().getClientSecret());
        params.add("redirect_uri", appProperties.getOauth().getGoogle().getRedirectUri());
        params.add("code", code);
        return params;
    }
}
