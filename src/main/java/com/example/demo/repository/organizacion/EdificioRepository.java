package com.example.demo.repository.organizacion;

import com.example.demo.domain.organizacion.Edificio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdificioRepository extends JpaRepository<Edificio, Long> {
    Page<Edificio> findByCiudadId(Long ciudadId, Pageable pageable);
}
