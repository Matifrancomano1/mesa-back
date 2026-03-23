package com.example.demo.dto.software;

import com.example.demo.dto.common.AuditoriaDto;
import com.example.demo.dto.hardware.EquipoDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class InstalacionSoftwareDto {
    private Long id;
    private EquipoDto equipo;
    private ProductoSoftwareDto producto;
    private LicenciaDto licencia;
    private LocalDate fechaInstalacion;
    private String observaciones;
    private boolean activo;
    private AuditoriaDto auditoria;
}
