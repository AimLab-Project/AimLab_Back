package com.aimlab.util;

import org.springframework.http.ResponseCookie;

public class CookieUtil {
    /**
     * ResponseCookie를 반환하는 유틸 메소드
     * @param name 쿠키 키
     * @param value 쿠키 값
     * @param maxAge 유효 시간
     * @return ResponseCookie
     */
    public static ResponseCookie getNewCookie(String name, String value, long maxAge){
        return ResponseCookie
                .from(name, value)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("none")
                .maxAge(maxAge)
                .build();
    }
}
