package com.example.demo.service.organizacion;

import com.example.demo.domain.organizacion.Circunscripcion;
import com.example.demo.domain.organizacion.Distrito;
import com.example.demo.dto.organizacion.DistritoDto;
import com.example.demo.dto.organizacion.DistritoRequest;
import com.example.demo.mapper.organizacion.OrganizacionMapper;
import com.example.demo.repository.organizacion.CircunscripcionRepository;
import com.example.demo.repository.organizacion.DistritoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DistritoService {

    private final DistritoRepository distritoRepository;
    private final CircunscripcionRepository circunscripcionRepository;
    private final OrganizacionMapper mapper;

    @Transactional(readOnly = true)
    public Page<DistritoDto> findAll(Long circunscripcionId, Boolean activo, Pageable pageable) {
        if (circunscripcionId != null && activo != null) {
            return distritoRepository.findByCircunscripcionIdAndActivo(circunscripcionId, activo, pageable).map(mapper::toDto);
        } else if (circunscripcionId != null) {
            return distritoRepository.findByCircunscripcionId(circunscripcionId, pageable).map(mapper::toDto);
        } else if (activo != null) {
            return distritoRepository.findByActivo(activo, pageable).map(mapper::toDto);
        }
        return distritoRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public DistritoDto findById(Long id) {
        return distritoRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Distrito no encontrado"));
    }

    @Transactional
    public DistritoDto create(DistritoRequest request) {
        if (distritoRepository.existsByCodigo(request.getCodigo())) {
            throw new IllegalStateException("Ya existe un distrito con ese código");
        }

        Circunscripcion circunscripcion = circunscripcionRepository.findById(request.getCircunscripcionId())
                .orElseThrow(() -> new EntityNotFoundException("Circunscripción no encontrada"));

        Distrito entity = new Distrito();
        entity.setNombre(request.getNombre());
        entity.setCodigo(request.getCodigo());
        entity.setCircunscripcion(circunscripcion);
        entity.setActivo(true);
        return mapper.toDto(distritoRepository.save(entity));
    }

    @Transactional
    public DistritoDto update(Long id, DistritoRequest request) {
        Distrito entity = distritoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Distrito no encontrado"));

        distritoRepository.findByCodigo(request.getCodigo())
                .filter(d -> !d.getId().equals(id))
                .ifPresent(d -> {
                    throw new IllegalStateException("El código ya está en uso");
                });

        Circunscripcion circunscripcion = circunscripcionRepository.findById(request.getCircunscripcionId())
                .orElseThrow(() -> new EntityNotFoundException("Circunscripción no encontrada"));

        entity.setNombre(request.getNombre());
        entity.setCodigo(request.getCodigo());
        entity.setCircunscripcion(circunscripcion);
        return mapper.toDto(distritoRepository.save(entity));
    }

    @Transactional
    public void softDelete(Long id) {
        Distrito entity = distritoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Distrito no encontrado"));
        entity.setActivo(false);
        distritoRepository.save(entity);
    }
}
