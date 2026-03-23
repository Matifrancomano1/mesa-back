package com.example.demo.dto.software;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LicenciaRequest {
    @NotBlank(message = "La clave es obligatoria")
    private String clave;

    @NotBlank(message = "El tipo de licencia es obligatorio")
    private String tipo;

    @NotNull(message = "La cantidad total es obligatoria")
    private Integer cantidadTotal;

    private LocalDate fechaVencimiento;

    @NotNull(message = "El producto es obligatorio")
    private Long productoId;
}
