package com.example.demo.controller.contrato;

import com.example.demo.dto.contrato.ProveedorDto;
import com.example.demo.dto.contrato.ProveedorRequest;
import com.example.demo.service.contrato.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService service;

    @GetMapping
    public ResponseEntity<Page<ProveedorDto>> getAll(
            @RequestParam(required = false) Boolean activo,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(activo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProveedorDto> create(@Valid @RequestBody ProveedorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProveedorDto> update(@PathVariable Long id, @Valid @RequestBody ProveedorRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
