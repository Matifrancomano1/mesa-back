package com.example.demo.service.organizacion;

import com.example.demo.domain.organizacion.Ciudad;
import com.example.demo.domain.organizacion.Distrito;
import com.example.demo.dto.organizacion.CiudadDto;
import com.example.demo.dto.organizacion.CiudadRequest;
import com.example.demo.mapper.organizacion.OrganizacionMapper;
import com.example.demo.repository.organizacion.CiudadRepository;
import com.example.demo.repository.organizacion.DistritoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CiudadService {

    private final CiudadRepository ciudadRepository;
    private final DistritoRepository distritoRepository;
    private final OrganizacionMapper mapper;

    @Transactional(readOnly = true)
    public Page<CiudadDto> findAll(Long distritoId, Pageable pageable) {
        if (distritoId != null) {
            return ciudadRepository.findByDistritoId(distritoId, pageable).map(mapper::toDto);
        }
        return ciudadRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public CiudadDto findById(Long id) {
        return ciudadRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Ciudad no encontrada"));
    }

    @Transactional
    public CiudadDto create(CiudadRequest request) {
        Distrito distrito = distritoRepository.findById(request.getDistritoId())
                .orElseThrow(() -> new EntityNotFoundException("Distrito no encontrado"));

        Ciudad entity = new Ciudad();
        entity.setNombre(request.getNombre());
        entity.setDistrito(distrito);
        return mapper.toDto(ciudadRepository.save(entity));
    }

    @Transactional
    public CiudadDto update(Long id, CiudadRequest request) {
        Ciudad entity = ciudadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ciudad no encontrada"));

        Distrito distrito = distritoRepository.findById(request.getDistritoId())
                .orElseThrow(() -> new EntityNotFoundException("Distrito no encontrado"));

        entity.setNombre(request.getNombre());
        entity.setDistrito(distrito);
        return mapper.toDto(ciudadRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!ciudadRepository.existsById(id)) {
            throw new EntityNotFoundException("Ciudad no encontrada");
        }
        // Assuming physical delete for now, if not we add 'activo' to Ciudad later
        ciudadRepository.deleteById(id);
    }
}
