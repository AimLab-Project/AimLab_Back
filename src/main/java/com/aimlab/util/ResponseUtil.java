package com.aimlab.util;

import com.aimlab.common.exception.ErrorCode;
import com.aimlab.dto.ErrorResponse;
import com.aimlab.dto.FailResponse;
import com.aimlab.dto.SuccessResponse;

import java.util.List;

public class ResponseUtil {
    public static <T> SuccessResponse <T> success(T data){
        return new SuccessResponse<>("success", data);
    }

    public static FailResponse fail(ErrorCode errorCode){
        return new FailResponse("fail", errorCode);
    }

    public static FailResponse fail(ErrorCode errorCode, List<String> params){
        return new FailResponse("fail", errorCode, params);
    }

    public static ErrorResponse error(String message){
        return new ErrorResponse("error", message);
    }

    public static ErrorResponse error(Exception exception){
        return new ErrorResponse("error", exception);
    }
}
