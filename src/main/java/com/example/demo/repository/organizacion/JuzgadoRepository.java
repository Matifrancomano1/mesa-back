package com.example.demo.repository.organizacion;

import com.example.demo.domain.organizacion.Juzgado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuzgadoRepository extends JpaRepository<Juzgado, Long> {

    @Query("SELECT j FROM Juzgado j " +
           "WHERE (:edificioId IS NULL OR j.edificio.id = :edificioId) " +
           "AND (:distritoId IS NULL OR j.edificio.ciudad.distrito.id = :distritoId) " +
           "AND (:activo IS NULL OR j.activo = :activo) " +
           "AND (:q IS NULL OR LOWER(j.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(j.codigo) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Juzgado> buscarPaginado(
            @Param("edificioId") Long edificioId,
            @Param("distritoId") Long distritoId,
            @Param("activo") Boolean activo,
            @Param("q") String q,
            Pageable pageable);

    Optional<Juzgado> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
