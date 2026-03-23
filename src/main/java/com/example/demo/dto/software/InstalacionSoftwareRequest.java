package com.example.demo.dto.software;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InstalacionSoftwareRequest {
    @NotNull(message = "El equipo es obligatorio")
    private Long equipoId;

    @NotNull(message = "El producto de software es obligatorio")
    private Long productoId;

    private Long licenciaId;

    private LocalDate fechaInstalacion;

    private String observaciones;
}
