package com.aimlab.dto;

import com.aimlab.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FailResponse {
    private final String status;
    private final FailData data;

    public FailResponse(String status, ErrorCode errorCode){
        this.status = status;
        this.data = new FailData(errorCode.getCode(), errorCode.getMessage());
    }

    public FailResponse(String status, ErrorCode errorCode, List<String> fields){
        this.status = status;
        this.data = new FailData(errorCode.getCode(), errorCode.getMessage(), fields);
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private static class FailData{
        private final int code;
        private final String message;
        private final List<String> fields;

        private FailData(int code, String message){
            this.code = code;
            this.message = message;
            this.fields = new ArrayList<>();
        }

        private FailData(int code, String message, List<String> fields){
            this.code = code;
            this.message = message;
            this.fields = fields;
        }
    }
}
