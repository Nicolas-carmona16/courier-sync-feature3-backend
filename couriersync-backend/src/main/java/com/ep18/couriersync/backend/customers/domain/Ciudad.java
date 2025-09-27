package com.ep18.couriersync.backend.customers.domain;

import com.ep18.couriersync.backend.common.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Tabla: public.tblciudades
 */
@Entity
@Table(name = "tblciudades", schema = "public")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Ciudad extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idCiudad;

    @Column(name = "nombre_ciudad", nullable = false, length = 30)
    private String nombreCiudad;

    /**
     * Columna FK en BD: "departamento" INTEGER NOT NULL
     * Constraint: departamento_ciudad -> tbldepartamentos(id_departamento)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "departamento",
            nullable = false,
            foreignKey = @ForeignKey(name = "departamento_ciudad")
    )
    private Departamento departamento;
}