package com.example.funds.transfer.exception;

public class ApiRequestException extends RuntimeException{

    public ApiRequestException(String message) {
        super(message);
    }
}
