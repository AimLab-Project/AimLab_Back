package com.aimlab.controller;

import com.aimlab.dto.SuccessResponse;
import com.aimlab.domain.TestEntity;
import com.aimlab.service.TestService;
import com.aimlab.util.ResponseUtil;
import com.aimlab.util.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "테스트")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TestController {
    private final TestService service;

    @GetMapping("/test")
    public ResponseEntity<SuccessResponse<TestEntity>> testApi(){
        TestEntity entity = service.getTestData();

        return ResponseEntity
                .ok()
                .body(ResponseUtil.success(entity));
    }

    @GetMapping("whoareyou")
    public ResponseEntity<SuccessResponse<String>> whoareyou(){
        String userId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new AuthorizationServiceException("인증정보를 확인할 수 없습니다"));
        return ResponseEntity
                .ok()
                .body(ResponseUtil.success("you are " + userId));
    }
}
