package com.example.demo.controller.organizacion;

import com.example.demo.dto.organizacion.EdificioDto;
import com.example.demo.dto.organizacion.EdificioRequest;
import com.example.demo.service.organizacion.EdificioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/edificios")
@RequiredArgsConstructor
public class EdificioController {

    private final EdificioService service;

    @GetMapping
    public ResponseEntity<Page<EdificioDto>> getAll(
            @RequestParam(required = false) Long ciudadId,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(ciudadId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EdificioDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EdificioDto> create(@Valid @RequestBody EdificioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EdificioDto> update(@PathVariable Long id, @Valid @RequestBody EdificioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
