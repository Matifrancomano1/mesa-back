package com.example.demo.controller.caso;

import com.example.demo.dto.caso.*;
import com.example.demo.service.caso.CasoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/casos")
@RequiredArgsConstructor
public class CasoController {

    private final CasoService casoService;

    @GetMapping
    public ResponseEntity<Page<CasoDto>> getAll(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long juzgadoId,
            @RequestParam(required = false) Long tecnicoId,
            @RequestParam(required = false) String prioridad,
            @RequestParam(required = false) String q,
            Pageable pageable) {
        return ResponseEntity.ok(casoService.findAll(estado, juzgadoId, tecnicoId, prioridad, q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CasoDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(casoService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CASO_CREAR')")
    public ResponseEntity<CasoDto> create(@Valid @RequestBody CasoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(casoService.create(request));
    }

    // --- State Machine ---
    @PatchMapping("/{id}/asignar")
    @PreAuthorize("hasAuthority('CASO_ASIGNAR')")
    public ResponseEntity<CasoDto> asignar(@PathVariable Long id, @Valid @RequestBody AsignarCasoRequest request) {
        return ResponseEntity.ok(casoService.asignar(id, request));
    }

    @PatchMapping("/{id}/iniciar")
    @PreAuthorize("hasAuthority('CASO_GESTIONAR')")
    public ResponseEntity<CasoDto> iniciar(@PathVariable Long id) {
        return ResponseEntity.ok(casoService.iniciar(id));
    }

    @PatchMapping("/{id}/cerrar")
    @PreAuthorize("hasAuthority('CASO_CERRAR')")
    public ResponseEntity<CasoDto> cerrar(@PathVariable Long id, @Valid @RequestBody CerrarCasoRequest request) {
        return ResponseEntity.ok(casoService.cerrar(id, request));
    }

    @PatchMapping("/{id}/reabrir")
    @PreAuthorize("hasAuthority('CASO_REABRIR')")
    public ResponseEntity<CasoDto> reabrir(@PathVariable Long id) {
        return ResponseEntity.ok(casoService.reabrir(id));
    }

    // --- Bitacora ---
    @GetMapping("/{id}/bitacora")
    public ResponseEntity<Page<BitacoraEntradaDto>> getBitacora(@PathVariable Long id, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdminOrTech = auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) || 
                                auth.getAuthorities().contains(new SimpleGrantedAuthority("CASO_GESTIONAR"));
                                
        return ResponseEntity.ok(casoService.getBitacora(id, isAdminOrTech, pageable));
    }

    @PostMapping("/{id}/bitacora")
    public ResponseEntity<BitacoraEntradaDto> addBitacoraEntry(@PathVariable Long id, @Valid @RequestBody BitacoraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(casoService.addBitacoraEntry(id, request));
    }
}
