package com.example.demo.dto.caso;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignarCasoRequest {
    @NotNull(message = "El técnico a asignar es obligatorio")
    private Long tecnicoId;
}
