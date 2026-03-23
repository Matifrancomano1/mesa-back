package com.example.demo.service.seguridad;

import com.example.demo.domain.seguridad.Funcion;
import com.example.demo.domain.seguridad.Rol;
import com.example.demo.dto.seguridad.RolDto;
import com.example.demo.dto.seguridad.RolRequest;
import com.example.demo.mapper.seguridad.SeguridadMapper;
import com.example.demo.repository.seguridad.FuncionRepository;
import com.example.demo.repository.seguridad.RolRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final FuncionRepository funcionRepository;
    private final SeguridadMapper mapper;

    @Transactional(readOnly = true)
    public Page<RolDto> findAll(Pageable pageable) {
        return rolRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public RolDto findById(Long id) {
        return rolRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id " + id));
    }

    @Transactional
    public RolDto create(RolRequest request) {
        if (rolRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new IllegalStateException("Ya existe un rol con el nombre: " + request.getNombre());
        }

        Rol rol = new Rol();
        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());
        rol.setActivo(true);
        
        rol.setFunciones(getFuncionesByIds(request.getFuncionIds()));
        
        return mapper.toDto(rolRepository.save(rol));
    }

    @Transactional
    public RolDto update(Long id, RolRequest request) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id " + id));

        rolRepository.findByNombre(request.getNombre())
                .filter(r -> !r.getId().equals(id))
                .ifPresent(r -> {
                    throw new IllegalStateException("Ya existe otro rol con el nombre: " + request.getNombre());
                });

        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());
        
        rol.setFunciones(getFuncionesByIds(request.getFuncionIds()));

        return mapper.toDto(rolRepository.save(rol));
    }

    @Transactional
    public void softDelete(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id " + id));
        rol.setActivo(false);
        rolRepository.save(rol);
    }

    private Set<Funcion> getFuncionesByIds(List<Long> ids) {
        Set<Funcion> funciones = new HashSet<>();
        if (ids != null && !ids.isEmpty()) {
            List<Funcion> found = funcionRepository.findAllById(ids);
            if (found.size() != ids.size()) {
                throw new EntityNotFoundException("Una o más funciones no fueron encontradas");
            }
            funciones.addAll(found);
        }
        return funciones;
    }
}
