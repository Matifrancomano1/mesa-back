package com.example.demo.dto.auth;

import com.example.demo.dto.seguridad.UsuarioResumenDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private UsuarioResumenDto usuario;
}
