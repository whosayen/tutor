package com.lectorie.lectorie.exception.custom;

public class EmailAlreadyInUseException extends RuntimeException implements IHasCode {
    int code;

    public EmailAlreadyInUseException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
