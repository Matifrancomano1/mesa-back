package com.example.demo.dto.caso;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BitacoraRequest {
    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;

    private boolean esPrivado;
}
