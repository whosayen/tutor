package com.lectorie.lectorie.exception.custom;

public class EmailVerificationTokenExpiredException extends RuntimeException implements IHasCode {
    int code;
    public EmailVerificationTokenExpiredException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
