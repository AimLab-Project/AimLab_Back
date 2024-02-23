package com.aimlab.controller;

import com.aimlab.common.ApiResponse;
import com.aimlab.dto.user.DuplicateCheckDto;
import com.aimlab.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 이메일 중복 체크 엔드포인트
     * @param email 이메일
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> checkDuplicateEmail(@PathVariable("email") @Email String email){
        boolean isExist = userService.isEmailExist(email);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(DuplicateCheckDto.Response.builder()
                        .isExist(isExist).build()));
    }

    /**
     * 닉네임 중복 체크 엔드포인트
     * @param nickname 닉네임
     */
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<?> checkDuplicateNickname(@PathVariable("nickname") @NotBlank String nickname){
        boolean isExist = userService.isNicknameExist(nickname);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(DuplicateCheckDto.Response.builder()
                        .isExist(isExist).build()));
    }
}
