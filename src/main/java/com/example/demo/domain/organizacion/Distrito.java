package com.example.demo.domain.organizacion;

import com.example.demo.domain.common.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "distritos", uniqueConstraints = @UniqueConstraint(columnNames = "codigo"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Distrito extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "circunscripcion_id", nullable = false)
    private Circunscripcion circunscripcion;
}
