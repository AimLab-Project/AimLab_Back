package com.aimlab.controller;

import com.aimlab.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public ResponseEntity<?> rootUrl(){
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("This is the RESTful API for AimSharp."));
    }
}