package com.ep18.couriersync.backend.common.audit;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Clase base para entidades que requieren auditoría de creación y actualización.
 * Proporciona los campos `createdAt` y `updatedAt` que se gestionan automáticamente.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity {

    @Transient
    private OffsetDateTime createdAt;

    @Transient
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}