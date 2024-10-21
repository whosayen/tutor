package com.lectorie.lectorie.exception.custom;

public class ScheduleNotFoundException extends RuntimeException implements IHasCode {
    int code;
    public ScheduleNotFoundException(String s, int code) {
        super(s);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
