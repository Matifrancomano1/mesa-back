package com.example.demo.repository.organizacion;

import com.example.demo.domain.organizacion.Distrito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Long> {
    Page<Distrito> findByActivo(boolean activo, Pageable pageable);
    Page<Distrito> findByCircunscripcionIdAndActivo(Long circunscripcionId, boolean activo, Pageable pageable);
    Page<Distrito> findByCircunscripcionId(Long circunscripcionId, Pageable pageable);
    Optional<Distrito> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
