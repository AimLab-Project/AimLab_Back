package com.aimlab.controller;

import com.aimlab.service.TestService;
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
    String testApi(){
        return service.getTestData();
    }
}
