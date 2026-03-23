package com.example.demo.dto.hardware;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModeloDto {
    private Long id;
    private String nombre;
    private MarcaDto marca;
    private TipoHardwareDto tipo;
    private boolean activo;
    private AuditoriaDto auditoria;
}
