package com.example.demo.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardDto {
    private long casosAbiertos;
    private long casosCerradosUltimoMes;
    private long equiposEnReparacion;
    private long licenciasVencidas;
    private long contratosPorVencer;
    private long contratosUrgentes;
}
