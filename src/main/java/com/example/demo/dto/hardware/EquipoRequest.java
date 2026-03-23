package com.example.demo.dto.hardware;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EquipoRequest {
    @NotBlank(message = "El número de inventario es obligatorio")
    private String inventario;

    private String serie;

    @NotNull(message = "El modelo es obligatorio")
    private Long modeloId;

    @NotBlank(message = "El estado es obligatorio")
    private String estado; // OPERATIVO, EN_REPARACION, etc.

    private String observaciones;

    private LocalDate fechaAdquisicion;

    private Long edificioId;

    private Long juzgadoId;
}
