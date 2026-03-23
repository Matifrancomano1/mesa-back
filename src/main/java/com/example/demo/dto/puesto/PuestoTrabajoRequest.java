package com.example.demo.dto.puesto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PuestoTrabajoRequest {
    @NotBlank(message = "El nombre del puesto es obligatorio")
    private String nombre;

    @NotNull(message = "El juzgado es obligatorio")
    private Long juzgadoId;

    private String responsableNombre;
    private String responsableCargo;

    private List<Long> equipoIds;
}
