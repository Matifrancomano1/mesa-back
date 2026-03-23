package com.example.demo.controller.contrato;

import com.example.demo.dto.contrato.ContratoDto;
import com.example.demo.dto.contrato.ContratoRequest;
import com.example.demo.service.contrato.ContratoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/contratos")
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoService service;

    @GetMapping
    public ResponseEntity<Page<ContratoDto>> getAll(
            @RequestParam(required = false) Long proveedorId,
            @RequestParam(required = false) String q,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(proveedorId, q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ContratoDto> create(@Valid @RequestBody ContratoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ContratoDto> update(@PathVariable Long id, @Valid @RequestBody ContratoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
