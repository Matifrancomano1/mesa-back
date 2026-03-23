package com.example.demo.controller.hardware;

import com.example.demo.dto.hardware.EquipoDto;
import com.example.demo.dto.hardware.EquipoHistorialDto;
import com.example.demo.dto.hardware.EquipoRequest;
import com.example.demo.service.hardware.EquipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoService service;

    @GetMapping
    public ResponseEntity<Page<EquipoDto>> getAll(
            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) Long tipoId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long juzgadoId,
            @RequestParam(required = false) Long edificioId,
            @RequestParam(required = false) String q,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(marcaId, tipoId, estado, juzgadoId, edificioId, q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('HARDWARE_GESTIONAR')")
    public ResponseEntity<EquipoDto> create(@Valid @RequestBody EquipoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('HARDWARE_GESTIONAR')")
    public ResponseEntity<EquipoDto> update(@PathVariable Long id, @Valid @RequestBody EquipoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('HARDWARE_GESTIONAR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/historial")
    @PreAuthorize("hasAuthority('HARDWARE_VER_HISTORIAL')")
    public ResponseEntity<Page<EquipoHistorialDto>> getHistorial(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(service.getHistorial(id, pageable));
    }
}
