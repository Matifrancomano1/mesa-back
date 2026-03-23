package com.example.demo.repository.hardware;

import com.example.demo.domain.hardware.Modelo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    Page<Modelo> findByMarcaId(Long marcaId, Pageable pageable);
    Page<Modelo> findByTipoId(Long tipoId, Pageable pageable);
}
