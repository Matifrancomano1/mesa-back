package com.example.demo.repository.software;

import com.example.demo.domain.software.InstalacionSoftware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstalacionSoftwareRepository extends JpaRepository<InstalacionSoftware, Long> {
    Page<InstalacionSoftware> findByEquipoId(Long equipoId, Pageable pageable);
    Page<InstalacionSoftware> findByLicenciaId(Long licenciaId, Pageable pageable);
    Page<InstalacionSoftware> findByProductoId(Long productoId, Pageable pageable);
    boolean existsByEquipoIdAndProductoId(Long equipoId, Long productoId);
}
