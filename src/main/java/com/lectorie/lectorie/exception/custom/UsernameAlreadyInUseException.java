package com.lectorie.lectorie.exception.custom;

public class UsernameAlreadyInUseException extends RuntimeException implements IHasCode {

    int code;

    public UsernameAlreadyInUseException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
