package com.coindcx.api.persistance.POJO.response;

import com.coindcx.api.persistance.POJO.response.ApiResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseSuccess extends ApiResponse {
    private Object data;
    private String message;
    private String code;

    public ApiResponseSuccess(Object body) {
        this.status = "success";
        this.message = "";
        this.data = body;
        this.code = "200";
    }

    public ApiResponseSuccess(Object body, String message) {
        this.status = "success";
        this.code = "200";
        this.message = message;
        this.data = body;
    }
}
