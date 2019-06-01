package com.coindcx.api.persistance.POJO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseError extends ApiResponse{
    String code;
    Object message;
    Object data;


    public ApiResponseError(Object error, String code) {
        this.message = error;
        this.code = code;
        this.status = "error";
        this.data = null;
    }
}
