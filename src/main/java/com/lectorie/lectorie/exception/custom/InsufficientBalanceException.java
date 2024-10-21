package com.lectorie.lectorie.exception.custom;

public class InsufficientBalanceException extends RuntimeException {
    int code;

    public InsufficientBalanceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}