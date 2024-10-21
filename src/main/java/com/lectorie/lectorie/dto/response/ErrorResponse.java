package com.lectorie.lectorie.dto.response;


public record ErrorResponse(

    int error,
    String message
) {

}
