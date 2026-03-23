package com.example.demo.controller.organizacion;

import com.example.demo.dto.organizacion.CircunscripcionDto;
import com.example.demo.dto.organizacion.CircunscripcionRequest;
import com.example.demo.service.organizacion.CircunscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/circunscripciones")
@RequiredArgsConstructor
public class CircunscripcionController {

    private final CircunscripcionService service;

    @GetMapping
    public ResponseEntity<Page<CircunscripcionDto>> getAll(
            @RequestParam(required = false) Boolean activo,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(activo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CircunscripcionDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CircunscripcionDto> create(@Valid @RequestBody CircunscripcionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CircunscripcionDto> update(@PathVariable Long id, @Valid @RequestBody CircunscripcionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
