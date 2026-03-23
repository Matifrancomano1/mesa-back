package com.example.demo.domain.software;

import com.example.demo.domain.common.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos_software")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductoSoftware extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 50)
    private String version;

    @Column(length = 150)
    private String fabricante;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private boolean requiereLicencia;
}
