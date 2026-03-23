package com.example.demo.service.contrato;

import com.example.demo.domain.contrato.Contrato;
import com.example.demo.domain.contrato.Proveedor;
import com.example.demo.dto.contrato.ContratoDto;
import com.example.demo.dto.contrato.ContratoRequest;
import com.example.demo.mapper.contrato.ContratoMapper;
import com.example.demo.repository.contrato.ContratoRepository;
import com.example.demo.repository.contrato.ProveedorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ContratoMapper mapper;

    @Transactional(readOnly = true)
    public Page<ContratoDto> findAll(Long proveedorId, String q, Pageable pageable) {
        return contratoRepository.buscarPaginado(proveedorId, q, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public ContratoDto findById(Long id) {
        return contratoRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Contrato no encontrado"));
    }

    @Transactional
    public ContratoDto create(ContratoRequest request) {
        if (contratoRepository.findByNumeroContrato(request.getNumeroContrato()).isPresent()) {
            throw new IllegalStateException("El número de contrato ya existe");
        }

        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));

        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }

        Contrato entity = new Contrato();
        entity.setNumeroContrato(request.getNumeroContrato());
        entity.setObjeto(request.getObjeto());
        entity.setProveedor(proveedor);
        entity.setFechaInicio(request.getFechaInicio());
        entity.setFechaFin(request.getFechaFin());
        entity.setDiasAlertaPorVencer(request.getDiasAlertaPorVencer());
        entity.setDiasAlertaUrgente(request.getDiasAlertaUrgente());
        entity.setActivo(true);
        return mapper.toDto(contratoRepository.save(entity));
    }

    @Transactional
    public ContratoDto update(Long id, ContratoRequest request) {
        Contrato entity = contratoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrato no encontrado"));

        contratoRepository.findByNumeroContrato(request.getNumeroContrato())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new IllegalStateException("El número de contrato ya existe");
                });

        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));

        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }

        entity.setNumeroContrato(request.getNumeroContrato());
        entity.setObjeto(request.getObjeto());
        entity.setProveedor(proveedor);
        entity.setFechaInicio(request.getFechaInicio());
        entity.setFechaFin(request.getFechaFin());
        entity.setDiasAlertaPorVencer(request.getDiasAlertaPorVencer());
        entity.setDiasAlertaUrgente(request.getDiasAlertaUrgente());
        return mapper.toDto(contratoRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Contrato entity = contratoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrato no encontrado"));
        entity.setActivo(false);
        contratoRepository.save(entity);
    }
}
