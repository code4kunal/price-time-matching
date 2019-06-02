package com.ordermatching.api.exception;

import com.ordermatching.api.persistence.POJO.response.ApiResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseError> handleCustomException(CustomException e) {
        return new ResponseEntity<>(new ApiResponseError(e.getMessage(), e.getCode()), HttpStatus.NOT_ACCEPTABLE);
    }
}
