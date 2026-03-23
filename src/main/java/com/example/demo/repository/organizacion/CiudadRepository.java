package com.example.demo.repository.organizacion;

import com.example.demo.domain.organizacion.Ciudad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    Page<Ciudad> findByDistritoId(Long distritoId, Pageable pageable);
}
