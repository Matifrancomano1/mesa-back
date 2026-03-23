package com.example.demo.dto.organizacion;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistritoDto {
    private Long id;
    private String nombre;
    private String codigo;
    private CircunscripcionDto circunscripcion;
    private boolean activo;
    private AuditoriaDto auditoria;
}
