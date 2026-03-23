package com.example.demo.repository.caso;

import com.example.demo.domain.caso.BitacoraEntrada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BitacoraEntradaRepository extends JpaRepository<BitacoraEntrada, Long> {
    Page<BitacoraEntrada> findByCasoIdAndEsPrivadoFalse(Long casoId, Pageable pageable);
    Page<BitacoraEntrada> findByCasoId(Long casoId, Pageable pageable);
}
