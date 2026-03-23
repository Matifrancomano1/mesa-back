package com.example.demo.dto.caso;

import com.example.demo.dto.common.AuditoriaDto;
import com.example.demo.dto.organizacion.JuzgadoDto;
import com.example.demo.dto.seguridad.UsuarioResumenDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CasoDto {
    private Long id;
    private String numeroCaso;
    private String titulo;
    private String descripcion;
    private String estado;
    private String prioridad;
    private String tipo;
    private JuzgadoDto juzgadoReclamante;
    private UsuarioResumenDto tecnicoAsignado;
    private AuditoriaDto auditoria;
}
