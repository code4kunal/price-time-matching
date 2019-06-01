package com.coindcx.api.constants;

public enum Errors {

    INVALID_INPUT("Please provide a valid input.");

    public String error;

    Errors(String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return this.error;
    }
}
