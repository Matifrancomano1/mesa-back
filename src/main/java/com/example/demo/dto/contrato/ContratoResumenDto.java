package com.example.demo.dto.contrato;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContratoResumenDto {
    private Long id;
    private String numeroContrato;
    private String objeto;
    private String proveedorRazonSocial;
    private String estadoAlerta;
}
