package com.example.demo.dto.contrato;

import com.example.demo.dto.common.AuditoriaDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ContratoDto {
    private Long id;
    private String numeroContrato;
    private String objeto;
    private ProveedorDto proveedor;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer diasAlertaPorVencer;
    private Integer diasAlertaUrgente;
    private String estadoAlerta; // VIGENTE, POR_VENCER, URGENTE, VENCIDO
    private Integer diasRestantes;
    private boolean activo;
    private AuditoriaDto auditoria;
}
