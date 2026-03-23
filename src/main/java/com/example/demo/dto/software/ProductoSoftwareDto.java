package com.example.demo.dto.software;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoSoftwareDto {
    private Long id;
    private String nombre;
    private String version;
    private String fabricante;
    private String descripcion;
    private boolean requiereLicencia;
    private boolean activo;
    private AuditoriaDto auditoria;
}
