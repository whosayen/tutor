package com.lectorie.lectorie.exception.custom;

public class MessageException extends RuntimeException implements IHasCode {
    int code;
    public MessageException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}