package com.example.demo.service.organizacion;

import com.example.demo.domain.organizacion.Ciudad;
import com.example.demo.domain.organizacion.Edificio;
import com.example.demo.dto.organizacion.EdificioDto;
import com.example.demo.dto.organizacion.EdificioRequest;
import com.example.demo.mapper.organizacion.OrganizacionMapper;
import com.example.demo.repository.organizacion.CiudadRepository;
import com.example.demo.repository.organizacion.EdificioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EdificioService {

    private final EdificioRepository edificioRepository;
    private final CiudadRepository ciudadRepository;
    private final OrganizacionMapper mapper;

    @Transactional(readOnly = true)
    public Page<EdificioDto> findAll(Long ciudadId, Pageable pageable) {
        if (ciudadId != null) {
            return edificioRepository.findByCiudadId(ciudadId, pageable).map(mapper::toDto);
        }
        return edificioRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public EdificioDto findById(Long id) {
        return edificioRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Edificio no encontrado"));
    }

    @Transactional
    public EdificioDto create(EdificioRequest request) {
        Ciudad ciudad = ciudadRepository.findById(request.getCiudadId())
                .orElseThrow(() -> new EntityNotFoundException("Ciudad no encontrada"));

        Edificio entity = new Edificio();
        entity.setNombre(request.getNombre());
        entity.setDireccion(request.getDireccion());
        entity.setCiudad(ciudad);
        return mapper.toDto(edificioRepository.save(entity));
    }

    @Transactional
    public EdificioDto update(Long id, EdificioRequest request) {
        Edificio entity = edificioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Edificio no encontrado"));

        Ciudad ciudad = ciudadRepository.findById(request.getCiudadId())
                .orElseThrow(() -> new EntityNotFoundException("Ciudad no encontrada"));

        entity.setNombre(request.getNombre());
        entity.setDireccion(request.getDireccion());
        entity.setCiudad(ciudad);
        return mapper.toDto(edificioRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!edificioRepository.existsById(id)) {
            throw new EntityNotFoundException("Edificio no encontrado");
        }
        edificioRepository.deleteById(id);
    }
}
