package com.example.demo.mapper.software;

import com.example.demo.domain.software.InstalacionSoftware;
import com.example.demo.domain.software.Licencia;
import com.example.demo.domain.software.ProductoSoftware;
import com.example.demo.dto.software.InstalacionSoftwareDto;
import com.example.demo.dto.software.LicenciaDto;
import com.example.demo.dto.software.ProductoSoftwareDto;
import com.example.demo.mapper.hardware.HardwareMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {HardwareMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SoftwareMapper {
    ProductoSoftwareDto toDto(ProductoSoftware entity);
    
    LicenciaDto toDto(Licencia entity);
    
    InstalacionSoftwareDto toDto(InstalacionSoftware entity);
}
