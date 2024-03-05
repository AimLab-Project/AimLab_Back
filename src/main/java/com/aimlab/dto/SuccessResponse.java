package com.aimlab.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Jsend 형식을 따르는 공통 JSON api 형식
 * <a href="https://github.com/omniti-labs/jsend">...</a>
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> {
    private final String status;
    private final T data;

    public SuccessResponse(String status, T data) {
        this.status = status;
        this.data = data;
    }
}
