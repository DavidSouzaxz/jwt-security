package com.authentication.jwt_security.dtos;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String email;
    private String password;
}
