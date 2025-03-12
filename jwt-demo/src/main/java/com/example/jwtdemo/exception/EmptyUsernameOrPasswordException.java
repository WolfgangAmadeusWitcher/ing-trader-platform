package com.example.jwtdemo.exception;

public class EmptyUsernameOrPasswordException extends RuntimeException {
    public EmptyUsernameOrPasswordException() {
        super(
                "Username or Password fields can not be empty!"
        );
    }
}
