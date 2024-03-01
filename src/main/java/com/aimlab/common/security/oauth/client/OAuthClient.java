package com.aimlab.common.security.oauth.client;

import com.aimlab.common.security.oauth.OAuthServerType;
import com.aimlab.common.security.oauth.model.OAuthUserDto;

public interface OAuthClient {
    OAuthServerType supportServer();

    OAuthUserDto fetch(String code);
}
