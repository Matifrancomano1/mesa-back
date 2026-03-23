package com.example.demo.dto.seguridad;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResumenDto {
    private Long id;
    private String username;
    private String nombreCompleto;
}
