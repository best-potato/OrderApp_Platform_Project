package com.sparta.orderapp.exception;

public class NoSignedUserException extends RuntimeException{
    public NoSignedUserException() { super("가입되지 않은 유저입니다.");}
}
