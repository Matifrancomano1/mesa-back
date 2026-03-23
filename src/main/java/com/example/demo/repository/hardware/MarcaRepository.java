package com.example.demo.repository.hardware;

import com.example.demo.domain.hardware.Marca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Page<Marca> findByActivo(boolean activo, Pageable pageable);
    Optional<Marca> findByNombre(String nombre);
}
