package com.example.demo.repository.caso;

import com.example.demo.domain.caso.Caso;
import com.example.demo.domain.caso.EstadoCaso;
import com.example.demo.domain.caso.PrioridadCaso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CasoRepository extends JpaRepository<Caso, Long> {

    @Query("SELECT c FROM Caso c " +
           "WHERE (:estado IS NULL OR c.estado = :estado) " +
           "AND (:juzgadoId IS NULL OR c.juzgadoReclamante.id = :juzgadoId) " +
           "AND (:tecnicoId IS NULL OR c.tecnicoAsignado.id = :tecnicoId) " +
           "AND (:prioridad IS NULL OR c.prioridad = :prioridad) " +
           "AND (:q IS NULL OR LOWER(c.numeroCaso) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(c.titulo) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Caso> buscarPaginado(
            @Param("estado") EstadoCaso estado,
            @Param("juzgadoId") Long juzgadoId,
            @Param("tecnicoId") Long tecnicoId,
            @Param("prioridad") PrioridadCaso prioridad,
            @Param("q") String q,
            Pageable pageable);

    Optional<Caso> findByNumeroCaso(String numeroCaso);
}
