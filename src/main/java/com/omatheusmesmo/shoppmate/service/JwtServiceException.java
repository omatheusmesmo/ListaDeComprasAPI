package com.omatheusmesmo.shoppmate.service;

public class JwtServiceException extends RuntimeException {
    public JwtServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
