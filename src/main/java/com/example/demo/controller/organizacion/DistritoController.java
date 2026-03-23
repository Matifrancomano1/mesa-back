package com.example.demo.controller.organizacion;

import com.example.demo.dto.organizacion.DistritoDto;
import com.example.demo.dto.organizacion.DistritoRequest;
import com.example.demo.service.organizacion.DistritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/distritos")
@RequiredArgsConstructor
public class DistritoController {

    private final DistritoService service;

    @GetMapping
    public ResponseEntity<Page<DistritoDto>> getAll(
            @RequestParam(required = false) Long circunscripcionId,
            @RequestParam(required = false) Boolean activo,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(circunscripcionId, activo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistritoDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DistritoDto> create(@Valid @RequestBody DistritoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DistritoDto> update(@PathVariable Long id, @Valid @RequestBody DistritoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
