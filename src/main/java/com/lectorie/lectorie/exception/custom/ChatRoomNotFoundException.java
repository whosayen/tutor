package com.lectorie.lectorie.exception.custom;

public class ChatRoomNotFoundException extends RuntimeException implements IHasCode {
    int code;
    public ChatRoomNotFoundException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}