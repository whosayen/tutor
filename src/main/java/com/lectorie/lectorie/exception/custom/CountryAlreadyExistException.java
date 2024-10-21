package com.lectorie.lectorie.exception.custom;

public class CountryAlreadyExistException extends RuntimeException implements IHasCode {
    int code;
    public CountryAlreadyExistException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}