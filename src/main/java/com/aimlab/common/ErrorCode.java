package com.aimlab.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode{
    /**
     * 일반 에러
     */
    INVALID_PARAMETER(-101, "잘못된 파라미터가 전달되었습니다.", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_PARAMETER(-102, "필수 파라미터 누락되었습니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_REQUEST(-103, "해당 리소스에 대한 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND_RESOURCE(-104, "요청하신 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    /**
     * 인증, 인가 관련된 에러
     */
    INVALID_TOKEN(-201, "토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(-202, "토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_SIGNATURE(-203, "JWT 서명이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_TOKEN(-204, "지원하지 않는 JWT 토큰 형식입니다.", HttpStatus.UNAUTHORIZED),
    NOT_EXIST_USER(-205, "등록된 이메일이 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD(-206, "비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATED_EMAIL(-207, "이미 사용중인 이메일이니다.", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
