package com.example.demo.repository.hardware;

import com.example.demo.domain.hardware.Equipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    @Query("SELECT e FROM Equipo e " +
           "WHERE (:marcaId IS NULL OR e.modelo.marca.id = :marcaId) " +
           "AND (:tipoId IS NULL OR e.modelo.tipo.id = :tipoId) " +
           "AND (:estado IS NULL OR e.estado = :estado) " +
           "AND (:juzgadoId IS NULL OR e.juzgadoAsignado.id = :juzgadoId) " +
           "AND (:edificioId IS NULL OR e.edificioUbicacion.id = :edificioId) " +
           "AND (:q IS NULL OR LOWER(e.inventario) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(e.serie) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Equipo> buscarPaginado(
            @Param("marcaId") Long marcaId,
            @Param("tipoId") Long tipoId,
            @Param("estado") String estado,
            @Param("juzgadoId") Long juzgadoId,
            @Param("edificioId") Long edificioId,
            @Param("q") String q,
            Pageable pageable);

    Optional<Equipo> findByInventario(String inventario);
    boolean existsByInventario(String inventario);
}
