package com.lectorie.lectorie.exception.custom;

public class LanguageDoesNotExistException extends RuntimeException {
    int code;

    public LanguageDoesNotExistException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}