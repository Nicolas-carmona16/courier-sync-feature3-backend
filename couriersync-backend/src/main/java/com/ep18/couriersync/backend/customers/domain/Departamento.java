package com.ep18.couriersync.backend.customers.domain;

import com.ep18.couriersync.backend.common.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * tbldepartamentos
 */
@Entity
@Table(name = "tbldepartamentos",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_departamento_nombre", columnNames = {"nombreDepartamento"})
        })
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Departamento extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDdepartamento")
    @EqualsAndHashCode.Include
    private Long idDepartamento;

    @Column(name = "nombreDepartamento", nullable = false, length = 120)
    private String nombreDepartamento;
}