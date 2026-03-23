package com.example.demo.dto.software;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LicenciaDto {
    private Long id;
    private String clave;
    private String tipo;
    private Integer cantidadTotal;
    private Integer cantidadEnUso;
    private LocalDate fechaVencimiento;
    private ProductoSoftwareDto producto;
    private boolean activo;
    private AuditoriaDto auditoria;
}
