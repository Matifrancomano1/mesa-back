package com.example.demo.controller.organizacion;

import com.example.demo.dto.organizacion.JuzgadoDto;
import com.example.demo.dto.organizacion.JuzgadoRequest;
import com.example.demo.service.organizacion.JuzgadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/juzgados")
@RequiredArgsConstructor
public class JuzgadoController {

    private final JuzgadoService service;

    @GetMapping
    public ResponseEntity<Page<JuzgadoDto>> getAll(
            @RequestParam(required = false) Long edificioId,
            @RequestParam(required = false) Long distritoId,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String q,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(edificioId, distritoId, activo, q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuzgadoDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<JuzgadoDto> create(@Valid @RequestBody JuzgadoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<JuzgadoDto> update(@PathVariable Long id, @Valid @RequestBody JuzgadoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
