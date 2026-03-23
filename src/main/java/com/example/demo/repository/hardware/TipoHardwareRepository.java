package com.example.demo.repository.hardware;

import com.example.demo.domain.hardware.TipoHardware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoHardwareRepository extends JpaRepository<TipoHardware, Long> {
    Page<TipoHardware> findByClaseId(Long claseId, Pageable pageable);
}
