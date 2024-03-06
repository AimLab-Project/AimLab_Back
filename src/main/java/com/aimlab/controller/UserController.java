package com.aimlab.controller;

import com.aimlab.dto.SuccessResponse;
import com.aimlab.dto.user.DuplicateCheckDto;
import com.aimlab.service.UserService;
import com.aimlab.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "4. 회원관리")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 이메일 중복 체크 엔드포인트(일반 회원가입)
     * @param email 이메일
     */
    @Operation(summary = "이메일 중복 체크", description = "일반 회원가입에 사용되는 이메일 중복 체크 엔드포인트(일반 회원가입 유저의 이메일만 확인)")
    @GetMapping("/email/{email}")
    public ResponseEntity<SuccessResponse<DuplicateCheckDto.Response>> checkDuplicateEmail(
            @PathVariable("email") @Email String email){

        boolean isExist = userService.isEmailExist(email);

        return ResponseEntity
                .ok()
                .body(ResponseUtil.success(DuplicateCheckDto.Response.builder()
                        .isExist(isExist).build()));
    }

    /**
     * 닉네임 중복 체크 엔드포인트
     * @param nickname 닉네임
     */
    @Operation(summary = "닉네임 중복 체크", description = "일반 회원가입에 사용되는 닉네임 중복 체크 엔드포인트")
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<SuccessResponse<DuplicateCheckDto.Response>> checkDuplicateNickname(
            @PathVariable("nickname") @NotBlank String nickname){

        boolean isExist = userService.isNicknameExist(nickname);

        return ResponseEntity
                .ok()
                .body(ResponseUtil.success(DuplicateCheckDto.Response.builder()
                        .isExist(isExist).build()));
    }
}
