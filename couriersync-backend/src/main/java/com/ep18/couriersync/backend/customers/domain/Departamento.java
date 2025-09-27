package com.ep18.couriersync.backend.customers.domain;

import com.ep18.couriersync.backend.common.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Tabla: public.tbldepartamentos
 */
@Entity
@Table(name = "tbldepartamentos", schema = "public")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Departamento extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departamento", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idDepartamento;

    @Column(name = "nombre_departamento", nullable = false, length = 30)
    private String nombreDepartamento;
}