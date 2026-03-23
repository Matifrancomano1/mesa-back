package com.example.demo.controller.hardware;

import com.example.demo.dto.hardware.*;
import com.example.demo.service.hardware.HardwareCatalogosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hardware")
@RequiredArgsConstructor
public class HardwareCatalogosController {

    private final HardwareCatalogosService service;

    // --- CLASES ---
    @GetMapping("/clases")
    public ResponseEntity<Page<ClaseHardwareDto>> getClases(@RequestParam(required = false) Boolean activo, Pageable pageable) {
        return ResponseEntity.ok(service.getClases(activo, pageable));
    }

    @PostMapping("/clases")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ClaseHardwareDto> createClase(@Valid @RequestBody ClaseHardwareRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createClase(request));
    }

    // --- TIPOS ---
    @GetMapping("/tipos")
    public ResponseEntity<Page<TipoHardwareDto>> getTipos(@RequestParam(required = false) Long claseId, Pageable pageable) {
        return ResponseEntity.ok(service.getTipos(claseId, pageable));
    }

    @PostMapping("/tipos")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TipoHardwareDto> createTipo(@Valid @RequestBody TipoHardwareRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTipo(request));
    }

    // --- MARCAS ---
    @GetMapping("/marcas")
    public ResponseEntity<Page<MarcaDto>> getMarcas(@RequestParam(required = false) Boolean activo, Pageable pageable) {
        return ResponseEntity.ok(service.getMarcas(activo, pageable));
    }

    @PostMapping("/marcas")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MarcaDto> createMarca(@Valid @RequestBody MarcaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createMarca(request));
    }

    // --- MODELOS ---
    @GetMapping("/modelos")
    public ResponseEntity<Page<ModeloDto>> getModelos(
            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) Long tipoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.getModelos(marcaId, tipoId, pageable));
    }

    @PostMapping("/modelos")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ModeloDto> createModelo(@Valid @RequestBody ModeloRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createModelo(request));
    }
}
