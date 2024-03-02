package com.aimlab.common.security.oauth;

/**
 * OAuth 서비스 제공자 타입
 */
public enum OAuthServerType {
    KAKAO,
    NAVER,
    GOOGLE;

    public static OAuthServerType fromName(String name){
        return OAuthServerType.valueOf(name.toUpperCase());
    }
}
