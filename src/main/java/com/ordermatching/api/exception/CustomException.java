package com.ordermatching.api.exception;

import com.ordermatching.api.constants.Errors;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
public class CustomException extends Exception{
    private static final Logger LOGGER = LogManager.getLogger(CustomException.class);

    private final String code;

    public CustomException(Errors message, String code) {
        super(message.error);
        this.code = code;
    }
}
