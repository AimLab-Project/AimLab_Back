package com.aimlab.common.security.oauth;

import com.aimlab.entity.OAuthUser;

public interface OAuthClient {
    OAuthServerType supportServer();

    OAuthUser fetch(String code);
}
