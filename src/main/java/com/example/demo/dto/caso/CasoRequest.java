package com.example.demo.dto.caso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CasoRequest {
    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "La prioridad es obligatoria")
    private String prioridad;

    @NotBlank(message = "El tipo de requerimiento es obligatorio")
    private String tipo;

    @NotNull(message = "El juzgado reclamante es obligatorio")
    private Long juzgadoId;
}
