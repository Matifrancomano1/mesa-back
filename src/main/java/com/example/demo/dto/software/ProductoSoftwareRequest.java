package com.example.demo.dto.software;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductoSoftwareRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    private String version;
    private String fabricante;
    private String descripcion;
    private boolean requiereLicencia;
}
