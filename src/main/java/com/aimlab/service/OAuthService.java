package com.aimlab.service;

import com.aimlab.common.security.oauth.OAuthServerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OAuthService {
    public void authenticate(OAuthServerType serverType, String code){

    }
}
