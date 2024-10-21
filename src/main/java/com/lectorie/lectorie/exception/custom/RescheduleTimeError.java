package com.lectorie.lectorie.exception.custom;

public class RescheduleTimeError extends RuntimeException implements IHasCode {
    int code;
    public RescheduleTimeError(String s, int code) {
        super(s);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
