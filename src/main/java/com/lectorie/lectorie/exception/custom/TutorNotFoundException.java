package com.lectorie.lectorie.exception.custom;

public class TutorNotFoundException extends RuntimeException implements IHasCode {
    int code;
    public TutorNotFoundException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
