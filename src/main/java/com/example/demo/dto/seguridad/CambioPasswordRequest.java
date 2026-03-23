package com.example.demo.dto.seguridad;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CambioPasswordRequest {
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String passwordActual;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    private String passwordNueva;
}
