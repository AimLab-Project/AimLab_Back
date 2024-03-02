package com.aimlab.common.security.oauth.client;

import com.aimlab.common.security.oauth.model.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface HttpExchangeHandler {
    @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoTokenDto fetchKakaoAccessToken(@RequestParam MultiValueMap<String, String> params);

    @PostExchange(url = "https://kapi.kakao.com/v2/user/me", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoUserDto fetchKakaoUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String accessToken);

    @GetExchange(url = "https://nid.naver.com/oauth2.0/token")
    NaverTokenDto fetchNaverAccessToken(@RequestParam MultiValueMap<String, String> params);

    @GetExchange(url = "https://openapi.naver.com/v1/nid/me")
    NaverUserDto fetchNaverUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String accessToken);

    @PostExchange(url = "https://oauth2.googleapis.com/token", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenDto fetchGoogleAccessToken(@RequestParam MultiValueMap<String, String> params);

    @GetExchange(url = "https://www.googleapis.com/oauth2/v1/userinfo")
    GoogleUserDto fetchGoogleUser(@RequestParam(name = HttpHeaders.AUTHORIZATION) String accessToken, @RequestParam("access_token") String rearAccessToken);
}
