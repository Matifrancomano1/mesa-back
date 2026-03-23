package com.example.demo.dto.hardware;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModeloRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "La marca es obligatoria")
    private Long marcaId;

    @NotNull(message = "El tipo de hardware es obligatorio")
    private Long tipoId;
}
