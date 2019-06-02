package com.coindcx.api.persistence.POJO.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseError extends ApiResponse {
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
