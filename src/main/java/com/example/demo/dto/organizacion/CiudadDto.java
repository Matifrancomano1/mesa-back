package com.example.demo.dto.organizacion;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CiudadDto {
    private Long id;
    private String nombre;
    private DistritoDto distrito;
    private boolean activo;
    private AuditoriaDto auditoria;
}
