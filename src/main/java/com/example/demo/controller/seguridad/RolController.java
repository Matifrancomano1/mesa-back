package com.example.demo.controller.seguridad;

import com.example.demo.dto.seguridad.RolDto;
import com.example.demo.dto.seguridad.RolRequest;
import com.example.demo.service.seguridad.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @GetMapping
    @PreAuthorize("hasAuthority('SEGURIDAD_VER')")
    public ResponseEntity<Page<RolDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(rolService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_VER')")
    public ResponseEntity<RolDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SEGURIDAD_GESTIONAR')")
    public ResponseEntity<RolDto> create(@Valid @RequestBody RolRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_GESTIONAR')")
    public ResponseEntity<RolDto> update(@PathVariable Long id, @Valid @RequestBody RolRequest request) {
        return ResponseEntity.ok(rolService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_GESTIONAR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
