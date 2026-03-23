package com.example.demo.domain.caso;

import com.example.demo.domain.common.BaseAuditableEntity;
import com.example.demo.domain.seguridad.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bitacora_casos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BitacoraEntrada extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caso_id", nullable = false)
    private Caso caso;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentario;

    @Column(nullable = false)
    private boolean esPrivado; // Internal note vs Public note

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;
}
