package com.ep18.couriersync.backend.customers.domain;

import com.ep18.couriersync.backend.common.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * tblciudades
 * PK: IDciudad
 * FK: IDdepartamento
 */
@Entity
@Table(
        name = "tblciudades",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_ciudad_nombre_por_departamento",
                        columnNames = {"nombreCiudad", "IDdepartamento"}
                )
        },
        indexes = {
                @Index(name = "idx_ciudad_departamento", columnList = "IDdepartamento")
        }
)
@Getter
@Setter
@ToString(exclude = {"departamento"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Ciudad extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDciudad")
    @EqualsAndHashCode.Include
    private Long idCiudad;

    @Column(name = "nombreCiudad", nullable = false, length = 150)
    private String nombreCiudad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "IDdepartamento",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_ciudad_departamento")
    )
    private Departamento departamento;
}