package com.example.demo.service.hardware;

import com.example.demo.domain.hardware.ClaseHardware;
import com.example.demo.domain.hardware.Marca;
import com.example.demo.domain.hardware.Modelo;
import com.example.demo.domain.hardware.TipoHardware;
import com.example.demo.dto.hardware.*;
import com.example.demo.mapper.hardware.HardwareMapper;
import com.example.demo.repository.hardware.ClaseHardwareRepository;
import com.example.demo.repository.hardware.MarcaRepository;
import com.example.demo.repository.hardware.ModeloRepository;
import com.example.demo.repository.hardware.TipoHardwareRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HardwareCatalogosService {

    private final ClaseHardwareRepository claseRepository;
    private final TipoHardwareRepository tipoRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final HardwareMapper mapper;

    // --- CLASES ---
    @Transactional(readOnly = true)
    public Page<ClaseHardwareDto> getClases(Boolean activo, Pageable pageable) {
        if (activo != null) {
            return claseRepository.findByActivo(activo, pageable).map(mapper::toDto);
        }
        return claseRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional
    public ClaseHardwareDto createClase(ClaseHardwareRequest request) {
        if (claseRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new IllegalStateException("La clase ya existe");
        }
        ClaseHardware entity = new ClaseHardware();
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(true);
        return mapper.toDto(claseRepository.save(entity));
    }

    // --- TIPOS ---
    @Transactional(readOnly = true)
    public Page<TipoHardwareDto> getTipos(Long claseId, Pageable pageable) {
        if (claseId != null) {
            return tipoRepository.findByClaseId(claseId, pageable).map(mapper::toDto);
        }
        return tipoRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional
    public TipoHardwareDto createTipo(TipoHardwareRequest request) {
        ClaseHardware clase = claseRepository.findById(request.getClaseId())
                .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));
        TipoHardware entity = new TipoHardware();
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setClase(clase);
        entity.setActivo(true);
        return mapper.toDto(tipoRepository.save(entity));
    }

    // --- MARCAS ---
    @Transactional(readOnly = true)
    public Page<MarcaDto> getMarcas(Boolean activo, Pageable pageable) {
        if (activo != null) {
            return marcaRepository.findByActivo(activo, pageable).map(mapper::toDto);
        }
        return marcaRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional
    public MarcaDto createMarca(MarcaRequest request) {
        if (marcaRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new IllegalStateException("La marca ya existe");
        }
        Marca entity = new Marca();
        entity.setNombre(request.getNombre());
        entity.setActivo(true);
        return mapper.toDto(marcaRepository.save(entity));
    }

    // --- MODELOS ---
    @Transactional(readOnly = true)
    public Page<ModeloDto> getModelos(Long marcaId, Long tipoId, Pageable pageable) {
        if (marcaId != null) {
            return modeloRepository.findByMarcaId(marcaId, pageable).map(mapper::toDto);
        }
        if (tipoId != null) {
            return modeloRepository.findByTipoId(tipoId, pageable).map(mapper::toDto);
        }
        return modeloRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional
    public ModeloDto createModelo(ModeloRequest request) {
        Marca marca = marcaRepository.findById(request.getMarcaId())
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"));
        TipoHardware tipo = tipoRepository.findById(request.getTipoId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo no encontrado"));

        Modelo entity = new Modelo();
        entity.setNombre(request.getNombre());
        entity.setMarca(marca);
        entity.setTipo(tipo);
        entity.setActivo(true);
        return mapper.toDto(modeloRepository.save(entity));
    }
}
