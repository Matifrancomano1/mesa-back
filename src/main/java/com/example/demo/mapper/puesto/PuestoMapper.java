package com.example.demo.mapper.puesto;

import com.example.demo.domain.puesto.PuestoTrabajo;
import com.example.demo.dto.puesto.PuestoTrabajoDto;
import com.example.demo.mapper.hardware.HardwareMapper;
import com.example.demo.mapper.organizacion.OrganizacionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {OrganizacionMapper.class, HardwareMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PuestoMapper {
    PuestoTrabajoDto toDto(PuestoTrabajo entity);
}
