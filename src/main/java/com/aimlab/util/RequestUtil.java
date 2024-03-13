package com.aimlab.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class RequestUtil {

    /**
     * Request Id 추출
     */
    public static String getRequestId(HttpServletRequest request){
        String requestId = request.getHeader("X-RequestID"); // nginx가 생성한 request id 먼저 검사
        if(requestId == null){
            requestId = UUID.randomUUID().toString();   // 없으면 랜덤 id 생성
        }
        return requestId;
    }
}
