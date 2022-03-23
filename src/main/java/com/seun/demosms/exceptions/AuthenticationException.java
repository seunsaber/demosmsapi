package com.seun.demosms.exceptions;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String errorMessage){
        super(errorMessage);
    }
}
