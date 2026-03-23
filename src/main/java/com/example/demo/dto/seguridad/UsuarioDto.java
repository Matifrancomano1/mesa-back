package com.example.demo.dto.seguridad;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UsuarioDto {
    private Long id;
    private String username;
    private String nombre;
    private String apellido;
    private String email;
    private List<RolDto> roles;
    private boolean activo;
    private AuditoriaDto auditoria;
}
