package com.example.demo.service.puesto;

import com.example.demo.domain.hardware.Equipo;
import com.example.demo.domain.organizacion.Juzgado;
import com.example.demo.domain.puesto.PuestoTrabajo;
import com.example.demo.dto.puesto.PuestoTrabajoDto;
import com.example.demo.dto.puesto.PuestoTrabajoRequest;
import com.example.demo.mapper.puesto.PuestoMapper;
import com.example.demo.repository.hardware.EquipoRepository;
import com.example.demo.repository.organizacion.JuzgadoRepository;
import com.example.demo.repository.puesto.PuestoTrabajoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PuestoTrabajoService {

    private final PuestoTrabajoRepository puestoRepository;
    private final JuzgadoRepository juzgadoRepository;
    private final EquipoRepository equipoRepository;
    private final PuestoMapper mapper;

    @Transactional(readOnly = true)
    public Page<PuestoTrabajoDto> findAll(Long juzgadoId, String q, Pageable pageable) {
        return puestoRepository.buscarPaginado(juzgadoId, q, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public PuestoTrabajoDto findById(Long id) {
        return puestoRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Puesto de trabajo no encontrado"));
    }

    @Transactional
    public PuestoTrabajoDto create(PuestoTrabajoRequest request) {
        Juzgado juzgado = juzgadoRepository.findById(request.getJuzgadoId())
                .orElseThrow(() -> new EntityNotFoundException("Juzgado no encontrado"));

        PuestoTrabajo entity = new PuestoTrabajo();
        entity.setNombre(request.getNombre());
        entity.setJuzgado(juzgado);
        entity.setResponsableNombre(request.getResponsableNombre());
        entity.setResponsableCargo(request.getResponsableCargo());
        entity.setActivo(true);
        
        asignarEquipos(entity, request.getEquipoIds());
        
        return mapper.toDto(puestoRepository.save(entity));
    }

    @Transactional
    public PuestoTrabajoDto update(Long id, PuestoTrabajoRequest request) {
        PuestoTrabajo entity = puestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Puesto de trabajo no encontrado"));

        Juzgado juzgado = juzgadoRepository.findById(request.getJuzgadoId())
                .orElseThrow(() -> new EntityNotFoundException("Juzgado no encontrado"));

        entity.setNombre(request.getNombre());
        entity.setJuzgado(juzgado);
        entity.setResponsableNombre(request.getResponsableNombre());
        entity.setResponsableCargo(request.getResponsableCargo());

        asignarEquipos(entity, request.getEquipoIds());

        return mapper.toDto(puestoRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        PuestoTrabajo entity = puestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Puesto de trabajo no encontrado"));
        entity.setActivo(false);
        puestoRepository.save(entity);
    }
    
    private void asignarEquipos(PuestoTrabajo entity, List<Long> equipoIds) {
        if (equipoIds != null && !equipoIds.isEmpty()) {
            List<Equipo> equipos = equipoRepository.findAllById(equipoIds);
            if (equipos.size() != equipoIds.size()) {
                throw new EntityNotFoundException("Uno o más equipos no fueron encontrados");
            }
            entity.setEquipos(equipos);
        } else {
            entity.getEquipos().clear();
        }
    }
}
