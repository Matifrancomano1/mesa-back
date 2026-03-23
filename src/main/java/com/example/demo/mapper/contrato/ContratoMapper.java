package com.example.demo.mapper.contrato;

import com.example.demo.domain.contrato.Contrato;
import com.example.demo.domain.contrato.Proveedor;
import com.example.demo.dto.contrato.ContratoDto;
import com.example.demo.dto.contrato.ContratoResumenDto;
import com.example.demo.dto.contrato.ProveedorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContratoMapper {
    ProveedorDto toDto(Proveedor entity);

    @Mapping(target = "estadoAlerta", expression = "java(calcularEstadoAlerta(entity))")
    @Mapping(target = "diasRestantes", expression = "java(calcularDiasRestantes(entity))")
    ContratoDto toDto(Contrato entity);

    @Mapping(target = "proveedorRazonSocial", source = "proveedor.razonSocial")
    @Mapping(target = "estadoAlerta", expression = "java(calcularEstadoAlerta(entity))")
    ContratoResumenDto toResumenDto(Contrato entity);

    default String calcularEstadoAlerta(Contrato entity) {
        if (entity == null || entity.getFechaFin() == null) return null;
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), entity.getFechaFin());
        if (dias < 0) return "VENCIDO";
        if (dias <= entity.getDiasAlertaUrgente()) return "URGENTE";
        if (dias <= entity.getDiasAlertaPorVencer()) return "POR_VENCER";
        return "VIGENTE";
    }

    default Integer calcularDiasRestantes(Contrato entity) {
        if (entity == null || entity.getFechaFin() == null) return null;
        return (int) Math.max(0, ChronoUnit.DAYS.between(LocalDate.now(), entity.getFechaFin()));
    }
}
