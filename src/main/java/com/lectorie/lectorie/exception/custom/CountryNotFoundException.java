package com.lectorie.lectorie.exception.custom;

public class CountryNotFoundException extends RuntimeException implements IHasCode {
    int code;
    public CountryNotFoundException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}