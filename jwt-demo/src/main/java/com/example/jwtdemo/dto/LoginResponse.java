package com.example.jwtdemo.dto;

public class LoginResponse {
    private String jwtToken;
    private String message;
    private long expiresIn;

    public LoginResponse() {

    }

    public LoginResponse(String token, String message, long expiresIn) {
        this.jwtToken = token;
        this.message = message;
        this.expiresIn = expiresIn;
    }

    public LoginResponse(String errorMessage){
        this.message = errorMessage;
        this.expiresIn = -1;
        this.jwtToken = null;
    }

    public String getJwtToken() {
        return jwtToken;
    }
    public void setJwtToken(String token) {
        this.jwtToken = token;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getExpiresIn() {
        return expiresIn;
    }
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
