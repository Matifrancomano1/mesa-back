package com.example.demo.dto.caso;

import com.example.demo.dto.common.AuditoriaDto;
import com.example.demo.dto.seguridad.UsuarioResumenDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BitacoraEntradaDto {
    private Long id;
    private String comentario;
    private boolean esPrivado;
    private UsuarioResumenDto autor;
    private AuditoriaDto auditoria;
}
