package com.aimlab.common.security.oauth;

import org.springframework.core.convert.converter.Converter;

public class OAuthServerTypeConverter implements Converter<String, OAuthServerType> {
    @Override
    public OAuthServerType convert(String source) {
        return OAuthServerType.fromName(source);
    }
}
