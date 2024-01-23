package com.aimlab.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {
    private int resultCode;
    private String msg;
    private Object data;
}
