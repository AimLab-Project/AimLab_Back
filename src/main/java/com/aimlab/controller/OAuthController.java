package com.aimlab.controller;

import com.aimlab.dto.oauth.OAuthLoginDto;
import com.aimlab.common.security.oauth.OAuthServerType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OAuthController {
    @PostMapping("/login/{oauthServerType}")
    public ResponseEntity<?> oauthLogin(@PathVariable OAuthServerType oauthServerType,
                                        @RequestBody OAuthLoginDto.Request request){
        
        System.out.println("oauthServerType = " + oauthServerType + ", request = " + request.getCode());

        return ResponseEntity
                .ok()
                .body("");
    }
}
