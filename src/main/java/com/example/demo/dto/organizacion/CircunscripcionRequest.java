package com.example.demo.dto.organizacion;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CircunscripcionRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El código es obligatorio")
    private String codigo;
}
