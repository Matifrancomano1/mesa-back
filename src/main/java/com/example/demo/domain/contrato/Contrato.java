package com.example.demo.domain.contrato;

import com.example.demo.domain.common.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "contratos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Contrato extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String numeroContrato;

    @Column(nullable = false, length = 200)
    private String objeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false)
    private Integer diasAlertaPorVencer;

    @Column(nullable = false)
    private Integer diasAlertaUrgente;
    
    // In service layer we compute: estadoAlerta y diasRestantes
}
