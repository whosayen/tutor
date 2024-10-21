package com.lectorie.lectorie.exception.custom;

public class BookingNotFoundException extends RuntimeException implements IHasCode {
    int code;
    public BookingNotFoundException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}

