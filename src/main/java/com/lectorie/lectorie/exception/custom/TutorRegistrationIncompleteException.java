package com.lectorie.lectorie.exception.custom;

public class TutorRegistrationIncompleteException extends RuntimeException implements IHasCode{

    int code;

    public TutorRegistrationIncompleteException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

}
