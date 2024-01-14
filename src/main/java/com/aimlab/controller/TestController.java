package com.aimlab.controller;

import com.aimlab.entity.TestEntity;
import com.aimlab.service.TestService;
import common.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class TestController {
    private final TestService service;

    @GetMapping("/test")
    public ApiResponse<Object> testApi(){
        TestEntity entity = service.getTestData();

        return ApiResponse.builder()
                .resultCode(200)
                .msg("성공")
                .data(entity).build();
    }
}
