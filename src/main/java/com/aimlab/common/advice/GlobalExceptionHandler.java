package com.aimlab.common.advice;

import com.aimlab.common.exception.ErrorCode;
import com.aimlab.common.exception.CustomException;
import com.aimlab.util.ResponseUtil;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> handleUserNameNotFound(UsernameNotFoundException exception){
        return getErrorResponse(ErrorCode.NOT_EXIST_USER);
    }

    /**
     * 잘못된 비밀번호
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleWrongPassword(BadCredentialsException exception){
        return getErrorResponse(ErrorCode.INVALID_PASSWORD);
    }

    /**
     * 잘못된 매개변수 형식 전달
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleWrongArgument(MethodArgumentNotValidException exception){
        return getErrorResponse(ErrorCode.INVALID_PARAMETER);
    }

    /**
     * CustomException 에러 핸들링
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException exception){
        return getErrorResponse(exception.getErrorCode());
    }


    /**
     * 런터임 오류
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException exception){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtil.error(exception));
    }


    private ResponseEntity<?> getErrorResponse(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ResponseUtil.fail(errorCode));
    }
}
