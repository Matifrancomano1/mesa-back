package com.example.demo.controller.seguridad;

import com.example.demo.dto.seguridad.AsignarRolesRequest;
import com.example.demo.dto.seguridad.CambioPasswordRequest;
import com.example.demo.dto.seguridad.UsuarioDto;
import com.example.demo.dto.seguridad.UsuarioRequest;
import com.example.demo.service.seguridad.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('SEGURIDAD_VER')")
    public ResponseEntity<Page<UsuarioDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_VER')")
    public ResponseEntity<UsuarioDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SEGURIDAD_GESTIONAR')")
    public ResponseEntity<UsuarioDto> create(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_GESTIONAR')")
    public ResponseEntity<UsuarioDto> update(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_GESTIONAR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('SEGURIDAD_GESTIONAR')")
    public ResponseEntity<UsuarioDto> asignarRoles(@PathVariable Long id, @Valid @RequestBody AsignarRolesRequest request) {
        return ResponseEntity.ok(usuarioService.asignarRoles(id, request));
    }

    @PatchMapping("/me/password")
    // Cualquier usuario logueado puede cambiar su propia contraseña
    public ResponseEntity<Void> cambiarPasswordActiva(Authentication authentication, @Valid @RequestBody CambioPasswordRequest request) {
        usuarioService.cambiarPassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }
}
