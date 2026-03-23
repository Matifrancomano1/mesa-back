package com.example.demo.mapper.caso;

import com.example.demo.domain.caso.BitacoraEntrada;
import com.example.demo.domain.caso.Caso;
import com.example.demo.dto.caso.BitacoraEntradaDto;
import com.example.demo.dto.caso.CasoDto;
import com.example.demo.mapper.organizacion.OrganizacionMapper;
import com.example.demo.mapper.seguridad.SeguridadMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {OrganizacionMapper.class, SeguridadMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CasoMapper {
    CasoDto toDto(Caso entity);

    @Mapping(target = "autor", source = "autor")
    BitacoraEntradaDto toDto(BitacoraEntrada entity);
}
