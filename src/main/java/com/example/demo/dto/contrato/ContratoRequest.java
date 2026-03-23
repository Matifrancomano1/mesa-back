package com.example.demo.dto.contrato;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ContratoRequest {
    @NotBlank(message = "El número de contrato es obligatorio")
    private String numeroContrato;

    @NotBlank(message = "El objeto del contrato es obligatorio")
    private String objeto;

    @NotNull(message = "El proveedor es obligatorio")
    private Long proveedorId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @NotNull(message = "Los días de alerta por vencer son obligatorios")
    private Integer diasAlertaPorVencer;

    @NotNull(message = "Los días de alerta urgente son obligatorios")
    private Integer diasAlertaUrgente;
}
