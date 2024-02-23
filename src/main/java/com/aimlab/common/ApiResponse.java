package com.aimlab.common;

import com.aimlab.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

/**
 * Jsend 형식을 따르는 공통 JSON api 형식
 * <a href="https://github.com/omniti-labs/jsend">...</a>
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private final String status;
    private final String message;
    private final Object data;

    private ApiResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse success(Object data){
        return new ApiResponse("success", null, data);
    }

    public static ApiResponse fail(ErrorCode errorCode){
        return new ApiResponse("fail", null, Map.of("code", errorCode.getCode(), "message", errorCode.getMessage()));
    }

    public static ApiResponse error(ErrorCode errorCode){
        return new ApiResponse("error", errorCode.getMessage(), null);
    }

    public static ApiResponse error(Exception exception){
        return new ApiResponse("error", exception.getMessage(), null);
    }
}
