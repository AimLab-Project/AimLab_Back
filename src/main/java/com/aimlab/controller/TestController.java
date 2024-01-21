package com.aimlab.controller;

import com.aimlab.entity.TestEntity;
import com.aimlab.service.TestService;
import com.aimlab.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class TestController {
    private final TestService service;

    @GetMapping("/test")
    public ResponseEntity<TestEntity> testApi(){
        TestEntity entity = service.getTestData();

        return ResponseEntity.ok().body(entity);
    }

    @GetMapping("whoareyou")
    public ResponseEntity<String> whoareyou(){
        try {
            String userId = SecurityUtil.getCurrentUserId().orElse("로그인 안함");
            return ResponseEntity.ok(userId);
        } catch (Exception e){
            return ResponseEntity.ok("로그인 안함");
        }
    }
}
