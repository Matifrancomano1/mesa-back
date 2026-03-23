package com.example.demo.dto.seguridad;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AsignarRolesRequest {
    @NotEmpty(message = "Debe enviar al menos un rol")
    private List<Long> rolIds;
}
