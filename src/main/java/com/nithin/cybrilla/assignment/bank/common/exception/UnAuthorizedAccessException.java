package com.nithin.cybrilla.assignment.bank.common.exception;

public class UnAuthorizedAccessException extends RuntimeException{

    public UnAuthorizedAccessException(String message){
        super(message);
    }
}
