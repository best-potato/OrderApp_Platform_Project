package com.sparta.orderapp.exception;

public class DuplicateNameException extends RuntimeException {
    public DuplicateNameException() {
        super("이미 사용중인 이름입니다.");
    }
}
