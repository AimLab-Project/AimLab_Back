package com.aimlab.common.logging;

import com.aimlab.util.RequestUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

/**
 * Rest API Access Logging Filter (모든 Http 요청 기록)
 */
@Order(1)
@Component
public class AccessLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AccessLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * Access Log 기록
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();    // 시작 시간

        // Request, Response 래핑
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
//        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // Request Id 등록
        String requestId = RequestUtil.getRequestId();
        MDC.put("request_id", requestId);

        // Request Logging
        String uri = request.getRequestURI();
        String ip = RequestUtil.getRequestIp();
        String method = request.getMethod();

        chain.doFilter(requestWrapper, response);

        int status = response.getStatus();

        long elapsedTime = System.currentTimeMillis() - startTime;

        logger.info("[{}] [{}] [{}] [{}] - [{}] {}ms", requestId, ip, method, uri, status, elapsedTime);

        MDC.clear();
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
