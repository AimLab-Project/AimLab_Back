package com.aimlab.common.security.oauth;

import com.aimlab.dto.oauth.KakaoTokenDto;
import com.aimlab.dto.oauth.KakaoUserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface KakaoHttpClient {
    @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoTokenDto.Response fetchAccessToken(@RequestParam MultiValueMap<String, String> params);

    @PostExchange(url = "https://kapi.kakao.com/v2/user/me", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoUserDto.Response fetchUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String accesToken);
}
