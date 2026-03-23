package com.example.demo.mapper.hardware;

import com.example.demo.domain.hardware.ClaseHardware;
import com.example.demo.domain.hardware.Equipo;
import com.example.demo.domain.hardware.Marca;
import com.example.demo.domain.hardware.Modelo;
import com.example.demo.domain.hardware.TipoHardware;
import com.example.demo.dto.hardware.ClaseHardwareDto;
import com.example.demo.dto.hardware.EquipoDto;
import com.example.demo.dto.hardware.MarcaDto;
import com.example.demo.dto.hardware.ModeloDto;
import com.example.demo.dto.hardware.TipoHardwareDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HardwareMapper {
    ClaseHardwareDto toDto(ClaseHardware entity);
    TipoHardwareDto toDto(TipoHardware entity);
    MarcaDto toDto(Marca entity);
    ModeloDto toDto(Modelo entity);
    EquipoDto toDto(Equipo entity);
}
