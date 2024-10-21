package com.lectorie.lectorie.exception.custom;

public class WrongOtpException extends RuntimeException implements IHasCode {

    int code;

    public WrongOtpException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
