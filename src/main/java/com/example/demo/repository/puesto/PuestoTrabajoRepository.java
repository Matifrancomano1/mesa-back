package com.example.demo.repository.puesto;

import com.example.demo.domain.puesto.PuestoTrabajo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PuestoTrabajoRepository extends JpaRepository<PuestoTrabajo, Long> {

    @Query("SELECT p FROM PuestoTrabajo p WHERE (:juzgadoId IS NULL OR p.juzgado.id = :juzgadoId) " +
           "AND (:q IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.responsableNombre) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<PuestoTrabajo> buscarPaginado(
            @Param("juzgadoId") Long juzgadoId,
            @Param("q") String q,
            Pageable pageable);
}
