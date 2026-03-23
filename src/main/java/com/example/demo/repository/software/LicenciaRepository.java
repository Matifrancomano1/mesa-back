package com.example.demo.repository.software;

import com.example.demo.domain.software.Licencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenciaRepository extends JpaRepository<Licencia, Long> {
    Page<Licencia> findByProductoId(Long productoId, Pageable pageable);
    Optional<Licencia> findByClave(String clave);
}
