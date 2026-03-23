package com.example.demo.service.organizacion;

import com.example.demo.domain.organizacion.Edificio;
import com.example.demo.domain.organizacion.Juzgado;
import com.example.demo.dto.organizacion.JuzgadoDto;
import com.example.demo.dto.organizacion.JuzgadoRequest;
import com.example.demo.mapper.organizacion.OrganizacionMapper;
import com.example.demo.repository.organizacion.EdificioRepository;
import com.example.demo.repository.organizacion.JuzgadoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JuzgadoService {

    private final JuzgadoRepository juzgadoRepository;
    private final EdificioRepository edificioRepository;
    private final OrganizacionMapper mapper;

    @Transactional(readOnly = true)
    public Page<JuzgadoDto> findAll(Long edificioId, Long distritoId, Boolean activo, String q, Pageable pageable) {
        return juzgadoRepository.buscarPaginado(edificioId, distritoId, activo, q, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public JuzgadoDto findById(Long id) {
        return juzgadoRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Juzgado no encontrado"));
    }

    @Transactional
    public JuzgadoDto create(JuzgadoRequest request) {
        if (juzgadoRepository.existsByCodigo(request.getCodigo())) {
            throw new IllegalStateException("El código de juzgado ya existe");
        }

        Edificio edificio = edificioRepository.findById(request.getEdificioId())
                .orElseThrow(() -> new EntityNotFoundException("Edificio no encontrado"));

        Juzgado entity = new Juzgado();
        entity.setNombre(request.getNombre());
        entity.setCodigo(request.getCodigo());
        entity.setEdificio(edificio);
        entity.setActivo(true);
        return mapper.toDto(juzgadoRepository.save(entity));
    }

    @Transactional
    public JuzgadoDto update(Long id, JuzgadoRequest request) {
        Juzgado entity = juzgadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Juzgado no encontrado"));

        juzgadoRepository.findByCodigo(request.getCodigo())
                .filter(j -> !j.getId().equals(id))
                .ifPresent(j -> {
                    throw new IllegalStateException("El código de juzgado ya está en uso");
                });

        Edificio edificio = edificioRepository.findById(request.getEdificioId())
                .orElseThrow(() -> new EntityNotFoundException("Edificio no encontrado"));

        entity.setNombre(request.getNombre());
        entity.setCodigo(request.getCodigo());
        entity.setEdificio(edificio);
        return mapper.toDto(juzgadoRepository.save(entity));
    }

    @Transactional
    public void softDelete(Long id) {
        Juzgado entity = juzgadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Juzgado no encontrado"));
        entity.setActivo(false);
        juzgadoRepository.save(entity);
    }
}
