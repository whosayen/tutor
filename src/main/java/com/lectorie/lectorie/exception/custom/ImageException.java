package com.lectorie.lectorie.exception.custom;

public class ImageException extends RuntimeException {
    int code;

    public ImageException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}