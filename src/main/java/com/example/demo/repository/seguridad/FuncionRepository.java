package com.example.demo.repository.seguridad;

import com.example.demo.domain.seguridad.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionRepository extends JpaRepository<Funcion, Long> {
    Optional<Funcion> findByCodigo(String codigo);
}
