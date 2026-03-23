package com.example.demo.dto.seguridad;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FuncionDto {
    private Long id;
    private String codigo;
    private String descripcion;
    private String modulo;
    private boolean activo;
}
