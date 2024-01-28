package com.aimlab.controller;

import com.aimlab.common.ApiResponse;
import com.aimlab.entity.TestEntity;
import com.aimlab.service.TestService;
import com.aimlab.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TestController {
    private final TestService service;

    @GetMapping("/test")
    public ResponseEntity<?> testApi(){
        TestEntity entity = service.getTestData();

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(entity));
    }

    @GetMapping("whoareyou")
    public ResponseEntity<?> whoareyou(){
        String userId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new AuthorizationServiceException("인증정보를 확인할 수 없습니다"));
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("you are " + userId));
    }
}
