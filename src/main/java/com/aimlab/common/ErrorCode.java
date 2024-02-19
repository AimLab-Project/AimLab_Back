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
    FORBIDDEN_REQUEST(-105, "해당 요청에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    /**
     * 인증, 인가 관련된 에러
     */
    INVALID_TOKEN(-201, "토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(-202, "토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_SIGNATURE(-203, "JWT 서명이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_TOKEN(-204, "지원하지 않는 JWT 토큰 형식입니다.", HttpStatus.UNAUTHORIZED),
    NOT_EXIST_USER(-205, "등록된 회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(-206, "비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATED_EMAIL(-207, "이미 사용중인 이메일입니다.", HttpStatus.BAD_REQUEST),
    INVALID_REFRESH_TOKEN(-208,"유효하지 않은 리프레시 토큰 입니다.", HttpStatus.UNAUTHORIZED),
    UNABLE_TO_SEND_EMAIL(-209, "이메일을 전송할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_VERIFICATION(-210, "이메일 인증 데이터를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_VERIFICATION_CODE(-211, "잘못된 이메일 인증 코드입니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_VERIFICATION_CODE(-212, "이메일 인증 코드가 만료되었습니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_EMAIL(-213, "인증되지 않은 이메일입니다.", HttpStatus.UNAUTHORIZED),
    VERIFICATION_ATTEMPTS_EXCEEDED(-214, "이메일 인증 횟수를 초과했습니다.", HttpStatus.UNAUTHORIZED);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
