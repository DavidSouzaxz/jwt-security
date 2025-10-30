package com.authentication.jwt_security.dtos;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private String password;
}
