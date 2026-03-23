package com.example.demo.mapper.organizacion;

import com.example.demo.domain.organizacion.Circunscripcion;    
import com.example.demo.domain.organizacion.Ciudad;
import com.example.demo.domain.organizacion.Distrito;
import com.example.demo.domain.organizacion.Edificio;
import com.example.demo.domain.organizacion.Juzgado;
import com.example.demo.dto.organizacion.CircunscripcionDto;
import com.example.demo.dto.organizacion.CiudadDto;
import com.example.demo.dto.organizacion.DistritoDto;
import com.example.demo.dto.organizacion.EdificioDto;
import com.example.demo.dto.organizacion.JuzgadoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizacionMapper {
    CircunscripcionDto toDto(Circunscripcion entity);
    DistritoDto toDto(Distrito entity);
    CiudadDto toDto(Ciudad entity);
    EdificioDto toDto(Edificio entity);
    JuzgadoDto toDto(Juzgado entity);
}
