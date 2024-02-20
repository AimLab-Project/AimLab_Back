package com.aimlab.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class DuplicateCheckDto {
    @Getter
    @Setter
    @Builder
    public static class Response implements ResponseDto{
        private boolean isExist;
    }
}
