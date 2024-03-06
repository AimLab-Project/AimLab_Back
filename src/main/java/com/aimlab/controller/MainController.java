package com.aimlab.controller;

import com.aimlab.dto.SuccessResponse;
import com.aimlab.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "1. 루트 경로")
@RestController
public class MainController {

    /**
     * Root URL
     */
    @Operation(summary = "Root Uri", description = "API 루트 경로 : REST API 서버 소개 JSON 응답")
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<SuccessResponse<String>> rootUrl(){
        return ResponseEntity
                .ok()
                .body(ResponseUtil.success("This is the RESTful API for AimSharp."));
    }
}
