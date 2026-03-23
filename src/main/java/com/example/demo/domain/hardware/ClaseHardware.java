package com.example.demo.domain.hardware;

import com.example.demo.domain.common.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hardware_clases")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaseHardware extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(length = 255)
    private String descripcion;
}
