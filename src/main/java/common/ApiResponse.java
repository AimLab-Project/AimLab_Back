package common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse <T>{
    private int resultCode;
    private String msg;
    private T data;
}
