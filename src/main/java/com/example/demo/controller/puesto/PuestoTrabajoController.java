package com.example.demo.controller.puesto;

import com.example.demo.dto.puesto.PuestoTrabajoDto;
import com.example.demo.dto.puesto.PuestoTrabajoRequest;
import com.example.demo.service.puesto.PuestoTrabajoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/puestos")
@RequiredArgsConstructor
public class PuestoTrabajoController {

    private final PuestoTrabajoService service;

    @GetMapping
    public ResponseEntity<Page<PuestoTrabajoDto>> getAll(
            @RequestParam(required = false) Long juzgadoId,
            @RequestParam(required = false) String q,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(juzgadoId, q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuestoTrabajoDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PuestoTrabajoDto> create(@Valid @RequestBody PuestoTrabajoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PuestoTrabajoDto> update(@PathVariable Long id, @Valid @RequestBody PuestoTrabajoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
