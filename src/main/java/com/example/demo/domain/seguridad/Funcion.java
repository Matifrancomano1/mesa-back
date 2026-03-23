package com.example.demo.domain.seguridad;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "funciones", uniqueConstraints = @UniqueConstraint(columnNames = "codigo"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Funcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String codigo;

    @Column(length = 300)
    private String descripcion;

    @Column(length = 80)
    private String modulo;

    @Column(nullable = false)
    private boolean activo = true;
}
