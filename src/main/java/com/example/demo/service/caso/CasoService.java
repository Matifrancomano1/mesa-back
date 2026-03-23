package com.example.demo.service.caso;

import com.example.demo.domain.caso.*;
import com.example.demo.domain.organizacion.Juzgado;
import com.example.demo.domain.seguridad.Usuario;
import com.example.demo.dto.caso.*;
import com.example.demo.mapper.caso.CasoMapper;
import com.example.demo.repository.caso.BitacoraEntradaRepository;
import com.example.demo.repository.caso.CasoRepository;
import com.example.demo.repository.organizacion.JuzgadoRepository;
import com.example.demo.repository.seguridad.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class CasoService {

    private final CasoRepository casoRepository;
    private final BitacoraEntradaRepository bitacoraRepository;
    private final JuzgadoRepository juzgadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CasoMapper mapper;

    @Transactional(readOnly = true)
    public Page<CasoDto> findAll(String estadoStr, Long juzgadoId, Long tecnicoId, String prioridadStr, String q, Pageable pageable) {
        EstadoCaso estado = estadoStr != null ? EstadoCaso.valueOf(estadoStr) : null;
        PrioridadCaso prioridad = prioridadStr != null ? PrioridadCaso.valueOf(prioridadStr) : null;
        return casoRepository.buscarPaginado(estado, juzgadoId, tecnicoId, prioridad, q, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public CasoDto findById(Long id) {
        return casoRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Caso no encontrado"));
    }

    @Transactional
    public CasoDto create(CasoRequest request) {
        Juzgado juzgado = juzgadoRepository.findById(request.getJuzgadoId())
                .orElseThrow(() -> new EntityNotFoundException("Juzgado no encontrado"));

        Caso entity = new Caso();
        entity.setTitulo(request.getTitulo());
        entity.setDescripcion(request.getDescripcion());
        entity.setPrioridad(PrioridadCaso.valueOf(request.getPrioridad()));
        entity.setTipo(TipoRequerimiento.valueOf(request.getTipo()));
        entity.setJuzgadoReclamante(juzgado);
        entity.setEstado(EstadoCaso.SOLICITADO);
        entity.setActivo(true);
        
        // Save first to get the ID, then generate the number
        entity = casoRepository.save(entity);
        entity.setNumeroCaso(String.format("CASO-%d-%05d", Year.now().getValue(), entity.getId()));
        
        return mapper.toDto(casoRepository.save(entity));
    }

    // --- State Machine ---
    @Transactional
    public CasoDto asignar(Long id, AsignarCasoRequest request) {
        Caso caso = casoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Caso no encontrado"));
                
        if (caso.getEstado() != EstadoCaso.SOLICITADO) {
            throw new IllegalStateException("Solo se pueden asignar casos en estado SOLICITADO");
        }

        Usuario tecnico = usuarioRepository.findById(request.getTecnicoId())
                .orElseThrow(() -> new EntityNotFoundException("Técnico no encontrado"));

        caso.setTecnicoAsignado(tecnico);
        caso.setEstado(EstadoCaso.ASIGNADO);
        
        agregarEntradaBitacoraSistema(caso, "Caso asignado al técnico: " + tecnico.getUsername());
        
        return mapper.toDto(casoRepository.save(caso));
    }

    @Transactional
    public CasoDto iniciar(Long id) {
        Caso caso = casoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Caso no encontrado"));
                
        if (caso.getEstado() != EstadoCaso.ASIGNADO) {
            throw new IllegalStateException("Solo se pueden iniciar casos en estado ASIGNADO");
        }

        caso.setEstado(EstadoCaso.EN_CURSO);
        agregarEntradaBitacoraSistema(caso, "El técnico ha comenzado a trabajar en el caso.");
        
        return mapper.toDto(casoRepository.save(caso));
    }

    @Transactional
    public CasoDto cerrar(Long id, CerrarCasoRequest request) {
        Caso caso = casoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Caso no encontrado"));
                
        if (caso.getEstado() != EstadoCaso.EN_CURSO) {
            throw new IllegalStateException("Solo se pueden cerrar casos en estado EN_CURSO");
        }

        caso.setEstado(EstadoCaso.CERRADO);
        agregarEntradaBitacoraSistema(caso, "Caso cerrado. Solución: " + request.getNotaCierre());
        
        return mapper.toDto(casoRepository.save(caso));
    }

    @Transactional
    public CasoDto reabrir(Long id) {
        Caso caso = casoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Caso no encontrado"));
                
        if (caso.getEstado() != EstadoCaso.CERRADO) {
            throw new IllegalStateException("Solo se pueden reabrir casos en estado CERRADO");
        }

        caso.setEstado(EstadoCaso.EN_CURSO);
        agregarEntradaBitacoraSistema(caso, "El caso ha sido reabierto por un supervisor/admin.");
        
        return mapper.toDto(casoRepository.save(caso));
    }

    // --- Bitacora ---
    @Transactional(readOnly = true)
    public Page<BitacoraEntradaDto> getBitacora(Long casoId, boolean incluirPrivados, Pageable pageable) {
        if (!casoRepository.existsById(casoId)) {
            throw new EntityNotFoundException("Caso no encontrado");
        }
        
        if (incluirPrivados) {
            return bitacoraRepository.findByCasoId(casoId, pageable).map(mapper::toDto);
        } else {
            return bitacoraRepository.findByCasoIdAndEsPrivadoFalse(casoId, pageable).map(mapper::toDto);
        }
    }

    @Transactional
    public BitacoraEntradaDto addBitacoraEntry(Long casoId, BitacoraRequest request) {
        Caso caso = casoRepository.findById(casoId)
                .orElseThrow(() -> new EntityNotFoundException("Caso no encontrado"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario autor = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario actual no encontrado"));

        BitacoraEntrada entrada = new BitacoraEntrada();
        entrada.setCaso(caso);
        entrada.setComentario(request.getComentario());
        entrada.setEsPrivado(request.isEsPrivado());
        entrada.setAutor(autor);
        entrada.setActivo(true);

        return mapper.toDto(bitacoraRepository.save(entrada));
    }
    
    // Internal helper for state transitions
    private void agregarEntradaBitacoraSistema(Caso caso, String comentario) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario autor = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario actual no encontrado"));
                
        BitacoraEntrada entrada = new BitacoraEntrada();
        entrada.setCaso(caso);
        entrada.setComentario("[SISTEMA] " + comentario);
        entrada.setEsPrivado(false);
        entrada.setAutor(autor);
        entrada.setActivo(true);
        bitacoraRepository.save(entrada);
    }
}
