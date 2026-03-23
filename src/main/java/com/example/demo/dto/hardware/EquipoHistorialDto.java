package com.example.demo.dto.hardware;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EquipoHistorialDto {
    private Long idRevision;
    private String tipoRevision; // Crear, Actualizar, Borrar
    private EquipoDto equipo;
    private AuditoriaDto auditoria;
}
