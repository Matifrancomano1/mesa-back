package com.example.demo.dto.hardware;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TipoHardwareDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private ClaseHardwareDto clase;
    private boolean activo;
    private AuditoriaDto auditoria;
}
