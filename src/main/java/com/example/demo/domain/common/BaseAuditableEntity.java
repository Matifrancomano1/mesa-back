package com.example.demo.domain.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditableEntity {

    @Column(nullable = false)
    private boolean activo = true;

    @CreatedBy
    @Column(updatable = false, length = 80)
    private String creadoPor;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime creadoEn;

    @LastModifiedBy
    @Column(length = 80)
    private String modificadoPor;

    @LastModifiedDate
    private LocalDateTime modificadoEn;
}
