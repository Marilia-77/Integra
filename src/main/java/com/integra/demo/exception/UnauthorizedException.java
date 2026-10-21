package com.integra.demo.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) { super(msg); }
}