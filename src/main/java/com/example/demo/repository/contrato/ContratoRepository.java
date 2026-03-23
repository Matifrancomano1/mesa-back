package com.example.demo.repository.contrato;

import com.example.demo.domain.contrato.Contrato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    @Query("SELECT c FROM Contrato c WHERE (:proveedorId IS NULL OR c.proveedor.id = :proveedorId) " +
           "AND (:q IS NULL OR LOWER(c.numeroContrato) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(c.objeto) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Contrato> buscarPaginado(
            @Param("proveedorId") Long proveedorId,
            @Param("q") String q,
            Pageable pageable);

    Optional<Contrato> findByNumeroContrato(String numeroContrato);
}
