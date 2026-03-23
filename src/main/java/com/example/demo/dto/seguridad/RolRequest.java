package com.example.demo.dto.seguridad;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RolRequest {
    @NotBlank(message = "El nombre del rol es obligatorio")
    private String nombre;

    private String descripcion;

    private List<Long> funcionIds;
}
