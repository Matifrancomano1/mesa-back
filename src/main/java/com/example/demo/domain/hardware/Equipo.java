package com.example.demo.domain.hardware;

import com.example.demo.domain.common.BaseAuditableEntity;
import com.example.demo.domain.organizacion.Edificio;
import com.example.demo.domain.organizacion.Juzgado;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;

@Entity
@Table(name = "equipos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited
public class Equipo extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String inventario;

    @Column(length = 100)
    private String serie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Modelo modelo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoEquipo estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    private LocalDate fechaAdquisicion;

    // Ubicacion física / asignacion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edificio_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Edificio edificioUbicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juzgado_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Juzgado juzgadoAsignado;
}
