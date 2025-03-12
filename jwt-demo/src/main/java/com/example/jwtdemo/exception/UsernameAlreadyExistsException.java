package com.example.jwtdemo.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(
                "Username already exists: " + message
        );
    }
}
