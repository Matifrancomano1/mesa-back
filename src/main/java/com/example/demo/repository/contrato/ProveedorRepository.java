package com.example.demo.repository.contrato;

import com.example.demo.domain.contrato.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Page<Proveedor> findByActivo(boolean activo, Pageable pageable);
    Optional<Proveedor> findByRuc(String ruc);
}
