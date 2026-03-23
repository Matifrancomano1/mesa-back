package com.example.demo.dto.organizacion;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EdificioDto {
    private Long id;
    private String nombre;
    private String direccion;
    private CiudadDto ciudad;
    private boolean activo;
    private AuditoriaDto auditoria;
}
