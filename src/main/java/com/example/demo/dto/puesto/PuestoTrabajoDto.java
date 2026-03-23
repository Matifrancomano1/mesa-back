package com.example.demo.dto.puesto;

import com.example.demo.dto.common.AuditoriaDto;
import com.example.demo.dto.hardware.EquipoDto;
import com.example.demo.dto.organizacion.JuzgadoDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PuestoTrabajoDto {
    private Long id;
    private String nombre;
    private JuzgadoDto juzgado;
    private String responsableNombre;
    private String responsableCargo;
    private List<EquipoDto> equipos;
    private boolean activo;
    private AuditoriaDto auditoria;
}
