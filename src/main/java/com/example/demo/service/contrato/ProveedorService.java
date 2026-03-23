package com.example.demo.service.contrato;

import com.example.demo.domain.contrato.Proveedor;
import com.example.demo.dto.contrato.ProveedorDto;
import com.example.demo.dto.contrato.ProveedorRequest;
import com.example.demo.mapper.contrato.ContratoMapper;
import com.example.demo.repository.contrato.ProveedorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ContratoMapper mapper;

    @Transactional(readOnly = true)
    public Page<ProveedorDto> findAll(Boolean activo, Pageable pageable) {
        if (activo != null) {
            return proveedorRepository.findByActivo(activo, pageable).map(mapper::toDto);
        }
        return proveedorRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public ProveedorDto findById(Long id) {
        return proveedorRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));
    }

    @Transactional
    public ProveedorDto create(ProveedorRequest request) {
        if (proveedorRepository.findByRuc(request.getRuc()).isPresent()) {
            throw new IllegalStateException("El RUC ya está registrado para otro proveedor");
        }
        Proveedor entity = new Proveedor();
        entity.setRazonSocial(request.getRazonSocial());
        entity.setRuc(request.getRuc());
        entity.setTelefono(request.getTelefono());
        entity.setEmail(request.getEmail());
        entity.setDireccion(request.getDireccion());
        entity.setActivo(true);
        return mapper.toDto(proveedorRepository.save(entity));
    }

    @Transactional
    public ProveedorDto update(Long id, ProveedorRequest request) {
        Proveedor entity = proveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));

        proveedorRepository.findByRuc(request.getRuc())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new IllegalStateException("El RUC ya está registrado para otro proveedor");
                });

        entity.setRazonSocial(request.getRazonSocial());
        entity.setRuc(request.getRuc());
        entity.setTelefono(request.getTelefono());
        entity.setEmail(request.getEmail());
        entity.setDireccion(request.getDireccion());
        return mapper.toDto(proveedorRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Proveedor entity = proveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));
        entity.setActivo(false);
        proveedorRepository.save(entity);
    }
}
