package com.example.demo.mapper.seguridad;

import com.example.demo.domain.seguridad.Funcion;
import com.example.demo.domain.seguridad.Rol;
import com.example.demo.domain.seguridad.Usuario;
import com.example.demo.dto.seguridad.FuncionDto;
import com.example.demo.dto.seguridad.RolDto;
import com.example.demo.dto.seguridad.UsuarioDto;
import com.example.demo.dto.seguridad.UsuarioResumenDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeguridadMapper {

    FuncionDto toDto(Funcion entity);

    RolDto toDto(Rol entity);

    UsuarioDto toDto(Usuario entity);

    @Mapping(target = "nombreCompleto", expression = "java(entity.getNombre() + \" \" + entity.getApellido())")
    UsuarioResumenDto toResumenDto(Usuario entity);
}
