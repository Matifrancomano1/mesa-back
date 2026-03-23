package com.example.demo.service.organizacion;

import com.example.demo.domain.organizacion.Circunscripcion;
import com.example.demo.dto.organizacion.CircunscripcionDto;
import com.example.demo.dto.organizacion.CircunscripcionRequest;
import com.example.demo.mapper.organizacion.OrganizacionMapper;
import com.example.demo.repository.organizacion.CircunscripcionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CircunscripcionService {

    private final CircunscripcionRepository repository;
    private final OrganizacionMapper mapper;

    @Transactional(readOnly = true)
    public Page<CircunscripcionDto> findAll(Boolean activo, Pageable pageable) {
        if (activo != null) {
            return repository.findByActivo(activo, pageable).map(mapper::toDto);
        }
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public CircunscripcionDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Circunscripción no encontrada"));
    }

    @Transactional
    public CircunscripcionDto create(CircunscripcionRequest request) {
        if (repository.existsByCodigo(request.getCodigo())) {
            throw new IllegalStateException("Ya existe una circunscripción con ese código");
        }
        Circunscripcion entity = new Circunscripcion();
        entity.setNombre(request.getNombre());
        entity.setCodigo(request.getCodigo());
        entity.setActivo(true);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public CircunscripcionDto update(Long id, CircunscripcionRequest request) {
        Circunscripcion entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Circunscripción no encontrada"));

        repository.findByCodigo(request.getCodigo())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new IllegalStateException("El código ya está en uso");
                });

        entity.setNombre(request.getNombre());
        entity.setCodigo(request.getCodigo());
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void softDelete(Long id) {
        Circunscripcion entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Circunscripción no encontrada"));
        entity.setActivo(false);
        repository.save(entity);
    }
}
