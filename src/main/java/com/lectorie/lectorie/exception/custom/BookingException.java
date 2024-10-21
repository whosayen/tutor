package com.lectorie.lectorie.exception.custom;

public class BookingException extends RuntimeException implements IHasCode {
    int code;
    public BookingException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}