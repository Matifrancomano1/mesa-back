package com.example.demo.service.dashboard;

import com.example.demo.domain.caso.EstadoCaso;
import com.example.demo.domain.hardware.EstadoEquipo;
import com.example.demo.dto.dashboard.DashboardDto;
import com.example.demo.repository.caso.CasoRepository;
import com.example.demo.repository.contrato.ContratoRepository;
import com.example.demo.repository.hardware.EquipoRepository;
import com.example.demo.repository.software.LicenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CasoRepository casoRepository;
    private final EquipoRepository equipoRepository;
    private final LicenciaRepository licenciaRepository;
    private final ContratoRepository contratoRepository;

    @Transactional(readOnly = true)
    public DashboardDto getDashboardMetrics() {
        LocalDateTime haceUnMes = LocalDateTime.now().minusMonths(1);
        LocalDate hoy = LocalDate.now();

        long casosAbiertos = casoRepository.findAll().stream()
                .filter(c -> c.getEstado() != EstadoCaso.CERRADO && c.isActivo())
                .count();

        long casosCerradosUltimoMes = casoRepository.findAll().stream()
                .filter(c -> c.getEstado() == EstadoCaso.CERRADO && 
                             c.getModificadoEn() != null && 
                             c.getModificadoEn().isAfter(haceUnMes) && 
                             c.isActivo())
                .count();

        long equiposEnReparacion = equipoRepository.findAll().stream()
                .filter(e -> e.getEstado() == EstadoEquipo.EN_REPARACION && e.isActivo())
                .count();

        long licenciasVencidas = licenciaRepository.findAll().stream()
                .filter(l -> l.getFechaVencimiento() != null && l.getFechaVencimiento().isBefore(hoy) && l.isActivo())
                .count();

        // Calculate contracts
        long contratosPorVencer = 0;
        long contratosUrgentes = 0;

        for (var contrato : contratoRepository.findAll()) {
            if (!contrato.isActivo() || contrato.getFechaFin() == null || contrato.getFechaFin().isBefore(hoy)) {
                continue; // Ignore already expired or inactive
            }
            long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(hoy, contrato.getFechaFin());
            if (daysLeft <= contrato.getDiasAlertaUrgente()) {
                contratosUrgentes++;
            } else if (daysLeft <= contrato.getDiasAlertaPorVencer()) {
                contratosPorVencer++;
            }
        }

        return DashboardDto.builder()
                .casosAbiertos(casosAbiertos)
                .casosCerradosUltimoMes(casosCerradosUltimoMes)
                .equiposEnReparacion(equiposEnReparacion)
                .licenciasVencidas(licenciasVencidas)
                .contratosPorVencer(contratosPorVencer)
                .contratosUrgentes(contratosUrgentes)
                .build();
    }
}
