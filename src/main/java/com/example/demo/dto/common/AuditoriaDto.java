package com.example.demo.dto.common;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditoriaDto {
    private String creadoPor;
    private LocalDateTime creadoEn;
    private String modificadoPor;
    private LocalDateTime modificadoEn;
}
