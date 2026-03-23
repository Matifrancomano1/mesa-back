package com.example.demo.service.hardware;

import com.example.demo.domain.hardware.Equipo;
import com.example.demo.domain.hardware.EstadoEquipo;
import com.example.demo.domain.hardware.Modelo;
import com.example.demo.domain.organizacion.Edificio;
import com.example.demo.domain.organizacion.Juzgado;
import com.example.demo.dto.hardware.EquipoDto;
import com.example.demo.dto.hardware.EquipoHistorialDto;
import com.example.demo.dto.hardware.EquipoRequest;
import com.example.demo.mapper.hardware.HardwareMapper;
import com.example.demo.repository.hardware.EquipoRepository;
import com.example.demo.repository.hardware.ModeloRepository;
import com.example.demo.repository.organizacion.EdificioRepository;
import com.example.demo.repository.organizacion.JuzgadoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.RevisionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final ModeloRepository modeloRepository;
    private final EdificioRepository edificioRepository;
    private final JuzgadoRepository juzgadoRepository;
    private final HardwareMapper mapper;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public Page<EquipoDto> findAll(Long marcaId, Long tipoId, String estado, Long juzgadoId, Long edificioId, String q, Pageable pageable) {
        return equipoRepository.buscarPaginado(marcaId, tipoId, estado, juzgadoId, edificioId, q, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public EquipoDto findById(Long id) {
        return equipoRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));
    }

    @Transactional
    public EquipoDto create(EquipoRequest request) {
        if (equipoRepository.existsByInventario(request.getInventario())) {
            throw new IllegalStateException("El número de inventario ya existe");
        }

        Modelo modelo = modeloRepository.findById(request.getModeloId())
                .orElseThrow(() -> new EntityNotFoundException("Modelo no encontrado"));

        Equipo entity = new Equipo();
        entity.setInventario(request.getInventario());
        entity.setSerie(request.getSerie());
        entity.setModelo(modelo);
        entity.setEstado(EstadoEquipo.valueOf(request.getEstado()));
        entity.setObservaciones(request.getObservaciones());
        entity.setFechaAdquisicion(request.getFechaAdquisicion());
        entity.setActivo(true);

        asignarUbicacion(entity, request.getEdificioId(), request.getJuzgadoId());

        return mapper.toDto(equipoRepository.save(entity));
    }

    @Transactional
    public EquipoDto update(Long id, EquipoRequest request) {
        Equipo entity = equipoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));

        equipoRepository.findByInventario(request.getInventario())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(e -> {
                    throw new IllegalStateException("El número de inventario ya está en uso");
                });

        Modelo modelo = modeloRepository.findById(request.getModeloId())
                .orElseThrow(() -> new EntityNotFoundException("Modelo no encontrado"));

        entity.setInventario(request.getInventario());
        entity.setSerie(request.getSerie());
        entity.setModelo(modelo);
        entity.setEstado(EstadoEquipo.valueOf(request.getEstado()));
        entity.setObservaciones(request.getObservaciones());
        entity.setFechaAdquisicion(request.getFechaAdquisicion());

        asignarUbicacion(entity, request.getEdificioId(), request.getJuzgadoId());

        return mapper.toDto(equipoRepository.save(entity));
    }

    @Transactional
    public void softDelete(Long id) {
        Equipo entity = equipoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));
        entity.setActivo(false);
        equipoRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Page<EquipoHistorialDto> getHistorial(Long equipoId, Pageable pageable) {
        if (!equipoRepository.existsById(equipoId)) {
            throw new EntityNotFoundException("Equipo no encontrado");
        }

        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        
        // Count total revisions for the given entity
        Number totalElementsCount = (Number) auditReader.createQuery()
                .forRevisionsOfEntity(Equipo.class, false, true)
                .add(AuditEntity.id().eq(equipoId))
                .addProjection(AuditEntity.revisionNumber().count())
                .getSingleResult();
        
        if (totalElementsCount == null || totalElementsCount.longValue() == 0) {
            return Page.empty(pageable);
        }

        // Fetch paginated versions
        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(Equipo.class, false, true)
                .add(AuditEntity.id().eq(equipoId))
                .addOrder(AuditEntity.revisionNumber().desc())
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        List<?> results = query.getResultList();
        List<EquipoHistorialDto> dtoList = new ArrayList<>();

        for (Object row : results) {
            Object[] array = (Object[]) row;
            Equipo equipoRev = (Equipo) array[0];
            // The second position usually holds the CustomRevisionEntity or DefaultRevisionEntity
            // We can get the revision ID from the third element which is the RevisionType, by querying the revision separately if we wanted, 
            // but Envers usually returns {Entity, RevisionEntity, RevisionType}
            Object revisionEntity = array[1]; // Custom Revision Entity not explicitly created yet, it's DefaultRevisionEntity
            RevisionType revType = (RevisionType) array[2];

            // In order to properly map properties from DefaultRevisionEntity we'd need reflection or mapping.
            // For simplicity, we just use the mapped Equipo.
            
            Long revId = 0L;
            try {
                java.lang.reflect.Method getIdMethod = revisionEntity.getClass().getMethod("getId");
                revId = ((Number) getIdMethod.invoke(revisionEntity)).longValue();
            } catch (Exception e) {
                 // ignore or log
            }

            EquipoHistorialDto dto = EquipoHistorialDto.builder()
                    .idRevision(revId)
                    .tipoRevision(revType.name())
                    .equipo(mapper.toDto(equipoRev))
                    .build();
            
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, pageable, totalElementsCount.longValue());
    }

    private void asignarUbicacion(Equipo entity, Long edificioId, Long juzgadoId) {
        if (edificioId != null) {
            Edificio edificio = edificioRepository.findById(edificioId)
                    .orElseThrow(() -> new EntityNotFoundException("Edificio no encontrado"));
            entity.setEdificioUbicacion(edificio);
        } else {
            entity.setEdificioUbicacion(null);
        }

        if (juzgadoId != null) {
            Juzgado juzgado = juzgadoRepository.findById(juzgadoId)
                    .orElseThrow(() -> new EntityNotFoundException("Juzgado no encontrado"));
            entity.setJuzgadoAsignado(juzgado);
            
            // Si el juzgado está en un edificio distinto, se podría sobreescribir o validar.
            // Para simplificar, asumimos que si asignan juzgado, también hereda su edificio si no mandaron edificioId.
            if (entity.getEdificioUbicacion() == null && juzgado.getEdificio() != null) {
                entity.setEdificioUbicacion(juzgado.getEdificio());
            }
            
        } else {
            entity.setJuzgadoAsignado(null);
        }
    }
}
