package com.example.demo.repository.hardware;

import com.example.demo.domain.hardware.ClaseHardware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClaseHardwareRepository extends JpaRepository<ClaseHardware, Long> {
    Page<ClaseHardware> findByActivo(boolean activo, Pageable pageable);
    Optional<ClaseHardware> findByNombre(String nombre);
}
