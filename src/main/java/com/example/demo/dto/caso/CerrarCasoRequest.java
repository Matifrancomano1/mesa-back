package com.example.demo.dto.caso;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CerrarCasoRequest {
    @NotBlank(message = "La nota de cierre (solución) es obligatoria")
    private String notaCierre;
}
