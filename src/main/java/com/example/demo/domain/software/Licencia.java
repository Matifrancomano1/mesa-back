package com.example.demo.domain.software;

import com.example.demo.domain.common.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "licencias")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Licencia extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150, unique = true)
    private String clave;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoLicencia tipo;

    @Column(nullable = false)
    private Integer cantidadTotal;

    @Column(nullable = false)
    private Integer cantidadEnUso;

    private LocalDate fechaVencimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoSoftware producto;

    @PrePersist
    public void prePersist() {
        if (cantidadEnUso == null) {
            cantidadEnUso = 0;
        }
    }
}
