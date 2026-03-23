package com.example.demo.dto.contrato;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProveedorRequest {
    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;
    
    @NotBlank(message = "El RUC es obligatorio")
    private String ruc;

    private String telefono;
    private String email;
    private String direccion;
}
