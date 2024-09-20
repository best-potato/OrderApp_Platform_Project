package com.sparta.orderapp.exception;

public class MethodNotAllowed extends RuntimeException {
    public MethodNotAllowed(String message) {
        super(message);
    }
}
