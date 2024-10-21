package com.lectorie.lectorie.exception.custom;

public class LessonNotFoundException extends RuntimeException {
    int code;

    public LessonNotFoundException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}