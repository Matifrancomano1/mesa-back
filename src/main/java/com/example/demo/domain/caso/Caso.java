package com.example.demo.domain.caso;

import com.example.demo.domain.common.BaseAuditableEntity;
import com.example.demo.domain.organizacion.Juzgado;
import com.example.demo.domain.seguridad.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDateTime;
import java.time.Year;

@Entity
@Table(name = "casos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited
public class Caso extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String numeroCaso;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoCaso estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PrioridadCaso prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoRequerimiento tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juzgado_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Juzgado juzgadoReclamante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Usuario tecnicoAsignado;
    
    // Auto-generate numeroCaso on creation
    @PrePersist
    public void generateNumeroCaso() {
        if (this.numeroCaso == null) {
            // To be strictly correct, this requires a DB sequence. We use a placeholder here and rewrite it in Service.
            // But if we let DB dictate, we can update it in a post-persist hook or from service layer.
            // For now, let's keep it null here, and assign it inside CasoService using a Sequence.
        }
    }
}
