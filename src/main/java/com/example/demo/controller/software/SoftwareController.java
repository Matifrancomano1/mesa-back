package com.example.demo.controller.software;

import com.example.demo.dto.software.*;
import com.example.demo.service.software.SoftwareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class SoftwareController {

    private final SoftwareService service;

    // --- PRODUCTOS ---
    @GetMapping("/software")
    public ResponseEntity<Page<ProductoSoftwareDto>> getProductos(@RequestParam(required = false) Boolean activo, Pageable pageable) {
        return ResponseEntity.ok(service.getProductos(activo, pageable));
    }

    @PostMapping("/software")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductoSoftwareDto> createProducto(@Valid @RequestBody ProductoSoftwareRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProducto(request));
    }

    // --- LICENCIAS ---
    @GetMapping("/licencias")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<LicenciaDto>> getLicencias(@RequestParam(required = false) Long productoId, Pageable pageable) {
        return ResponseEntity.ok(service.getLicencias(productoId, pageable));
    }

    @PostMapping("/licencias")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<LicenciaDto> createLicencia(@Valid @RequestBody LicenciaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createLicencia(request));
    }

    // --- INSTALACIONES ---
    @GetMapping("/instalaciones")
    public ResponseEntity<Page<InstalacionSoftwareDto>> getInstalaciones(
            @RequestParam(required = false) Long equipoId,
            @RequestParam(required = false) Long licenciaId,
            @RequestParam(required = false) Long productoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.getInstalaciones(equipoId, licenciaId, productoId, pageable));
    }

    @PostMapping("/instalaciones")
    @PreAuthorize("hasAuthority('HARDWARE_GESTIONAR')")
    public ResponseEntity<InstalacionSoftwareDto> createInstalacion(@Valid @RequestBody InstalacionSoftwareRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createInstalacion(request));
    }

    @DeleteMapping("/instalaciones/{id}")
    @PreAuthorize("hasAuthority('HARDWARE_GESTIONAR')")
    public ResponseEntity<Void> deleteInstalacion(@PathVariable Long id) {
        service.deleteInstalacion(id);
        return ResponseEntity.noContent().build();
    }
}
