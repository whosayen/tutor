package com.lectorie.lectorie.exception.custom;

public class EmailIsNotVerifiedException extends RuntimeException implements IHasCode {
    int code;

    public EmailIsNotVerifiedException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
