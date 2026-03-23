package com.example.demo.dto.hardware;

import com.example.demo.dto.common.AuditoriaDto;
import com.example.demo.dto.organizacion.EdificioDto;
import com.example.demo.dto.organizacion.JuzgadoDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EquipoDto {
    private Long id;
    private String inventario;
    private String serie;
    private ModeloDto modelo;
    private String estado;
    private String observaciones;
    private LocalDate fechaAdquisicion;
    private EdificioDto edificioUbicacion;
    private JuzgadoDto juzgadoAsignado;
    private AuditoriaDto auditoria;
}
