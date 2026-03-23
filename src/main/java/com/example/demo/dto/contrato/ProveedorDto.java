package com.example.demo.dto.contrato;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProveedorDto {
    private Long id;
    private String razonSocial;
    private String ruc;
    private String telefono;
    private String email;
    private String direccion;
    private boolean activo;
    private AuditoriaDto auditoria;
}
