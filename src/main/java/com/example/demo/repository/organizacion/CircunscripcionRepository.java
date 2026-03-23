package com.example.demo.repository.organizacion;

import com.example.demo.domain.organizacion.Circunscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CircunscripcionRepository extends JpaRepository<Circunscripcion, Long> {
    Page<Circunscripcion> findByActivo(boolean activo, Pageable pageable);
    Optional<Circunscripcion> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
