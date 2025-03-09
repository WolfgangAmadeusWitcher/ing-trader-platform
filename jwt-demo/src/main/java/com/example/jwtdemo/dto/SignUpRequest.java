package com.example.jwtdemo.dto;

import com.example.jwtdemo.model.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String username;
    private String password;

    public SignUpRequest(){

    }

    public SignUpRequest(String username, String password, UserRole userRole) {
        this.username = username;
        this.password = password;
    }
}
