package com.example.demo.domain.software;

import com.example.demo.domain.common.BaseAuditableEntity;
import com.example.demo.domain.hardware.Equipo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "instalaciones_software")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InstalacionSoftware extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoSoftware producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "licencia_id")
    private Licencia licencia;

    @Column(nullable = false)
    private LocalDate fechaInstalacion;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
}
