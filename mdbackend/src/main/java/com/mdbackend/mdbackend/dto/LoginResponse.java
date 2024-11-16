package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private Integer id;

    public LoginResponse(String token, Integer id) {
        this.token = token;
        this.id = id;
    }
}
