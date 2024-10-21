package com.lectorie.lectorie.exception.custom;

public class StripeAccountNotFoundException extends RuntimeException implements IHasCode {
    int code;
    public StripeAccountNotFoundException(String s, int code) {
        super(s);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
