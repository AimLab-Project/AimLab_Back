package com.aimlab.controller;

import com.aimlab.dto.SuccessResponse;
import com.aimlab.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    /**
     * Root URL
     */
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<SuccessResponse<String>> rootUrl(){
        return ResponseEntity
                .ok()
                .body(ResponseUtil.success("This is the RESTful API for AimSharp."));
    }
}
