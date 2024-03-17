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

    /**
     * Client IP 추출
     */
    public static String getRequestIp(HttpServletRequest request){
        String ip = null;

        ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")){
            ip = "127.0.0.1";
        }

        return ip;
    }
}
