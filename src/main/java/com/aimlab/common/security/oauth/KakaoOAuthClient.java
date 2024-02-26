package com.aimlab.common.security.oauth;

import com.aimlab.entity.OAuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClient{
    @Override
    public OAuthServerType supportServer() {
        return OAuthServerType.KAKAO;
    }

    @Override
    public OAuthUser fetch(String code) {
        return null;
    }
}
