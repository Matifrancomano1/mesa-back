package com.example.demo.repository.software;

import com.example.demo.domain.software.ProductoSoftware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoSoftwareRepository extends JpaRepository<ProductoSoftware, Long> {
    Page<ProductoSoftware> findByActivo(boolean activo, Pageable pageable);
    Optional<ProductoSoftware> findByNombreAndVersion(String nombre, String version);
}
