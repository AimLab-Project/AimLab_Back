package com.aimlab.common.logging;

import com.aimlab.util.RequestUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Request ID 생성해 쓰레드 로컬에 저장하는 Filter
 */
class RequestIdFilter extends OncePerRequestFilter {

    /**
     * Request 식별자를 쓰레드 로컬에 저장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MDC.put("request_id", RequestUtil.getRequestId(request));
        filterChain.doFilter(request, response);
        MDC.clear();
    }
}
