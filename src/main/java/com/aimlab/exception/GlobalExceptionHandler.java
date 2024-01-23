package com.aimlab.exception;

import com.aimlab.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리 핸들러
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 존재하지 않는 이메일
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNameNotFound(UsernameNotFoundException exception){
        return getExceptionResponse(exception);
    }

    /**
     * 잘못된 비밀번호
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleWrongPassword(BadCredentialsException exception){
        return getExceptionResponse(exception);
    }

    /**
     * 잘못된 매개변수 형식 전달
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleWrongArgument(MethodArgumentNotValidException exception){
        return getExceptionResponse("잘못된 매개변수 형식입니다");
    }

    /**
     * 런터임 오류
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException exception){
        return getExceptionResponse(exception);
    }


    /**
     * 예외 처리 시 공통적으로 생성되는 응답데이터를 반환
     */
    private ResponseEntity<ApiResponse> getExceptionResponse(Exception exception){
        return ResponseEntity.ok(ApiResponse.builder()
                .msg(exception.getMessage())
                .resultCode(200).build());
    }
    private ResponseEntity<ApiResponse> getExceptionResponse(String msg){
        return ResponseEntity.ok(ApiResponse.builder()
                .msg(msg)
                .resultCode(200).build());
    }
}
