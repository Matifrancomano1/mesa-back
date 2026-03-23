package com.example.demo.dto.seguridad;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RolDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private List<FuncionDto> funciones;
    private boolean activo;
    private AuditoriaDto auditoria;
}
