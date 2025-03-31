package com.microservice.auth.http.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
