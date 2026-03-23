package com.example.demo.dto.hardware;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MarcaRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
}
