package com.ordermatching.api.constants;

public enum Errors {

    INVALID_INPUT("Please provide a valid input."),
    INVALID_SIDE("Invalid side. Please provide a valid input."),
    INVALID_PRICE("Invalid price. Please provide a valid input."),
    INVALID_SIZE("Invalid size. Please provide a valid input."),
    INVALID_TIME("Invalid time. Please provie a valid input.");

    public String error;

    Errors(String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return this.error;
    }
}
