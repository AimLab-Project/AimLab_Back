package com.aimlab.common.security.oauth.client;

import com.aimlab.common.security.oauth.OAuthServerType;
import com.aimlab.common.security.oauth.model.OAuthUserDto;

public interface OAuthClient {
    OAuthServerType supportServer();

    String getLoginRedirectUri();

    OAuthUserDto fetch(String code);
}
