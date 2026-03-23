package com.example.demo.controller.organizacion;

import com.example.demo.dto.organizacion.CiudadDto;
import com.example.demo.dto.organizacion.CiudadRequest;
import com.example.demo.service.organizacion.CiudadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/ciudades")
@RequiredArgsConstructor
public class CiudadController {

    private final CiudadService service;

    @GetMapping
    public ResponseEntity<Page<CiudadDto>> getAll(
            @RequestParam(required = false) Long distritoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(distritoId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CiudadDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CiudadDto> create(@Valid @RequestBody CiudadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CiudadDto> update(@PathVariable Long id, @Valid @RequestBody CiudadRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
