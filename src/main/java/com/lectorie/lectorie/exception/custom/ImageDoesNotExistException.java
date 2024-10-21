package com.lectorie.lectorie.exception.custom;

public class ImageDoesNotExistException extends RuntimeException {
    int code;

    public ImageDoesNotExistException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}