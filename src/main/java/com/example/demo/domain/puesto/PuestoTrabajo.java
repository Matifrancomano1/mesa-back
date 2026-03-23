package com.example.demo.domain.puesto;

import com.example.demo.domain.common.BaseAuditableEntity;
import com.example.demo.domain.hardware.Equipo;
import com.example.demo.domain.organizacion.Juzgado;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "puestos_trabajo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PuestoTrabajo extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juzgado_id", nullable = false)
    private Juzgado juzgado;

    @Column(length = 150)
    private String responsableNombre;

    @Column(length = 100)
    private String responsableCargo;

    @ManyToMany
    @JoinTable(
        name = "puesto_equipos",
        joinColumns = @JoinColumn(name = "puesto_id"),
        inverseJoinColumns = @JoinColumn(name = "equipo_id")
    )
    @Builder.Default
    private List<Equipo> equipos = new ArrayList<>();
}
