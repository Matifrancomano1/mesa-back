package com.example.demo.dto.organizacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EdificioRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String direccion;

    @NotNull(message = "La ciudad es obligatoria")
    private Long ciudadId;
}
