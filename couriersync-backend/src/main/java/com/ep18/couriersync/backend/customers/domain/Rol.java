package com.ep18.couriersync.backend.customers.domain;

import com.ep18.couriersync.backend.common.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * tblroles
 * PK: IDrol
 */
@Entity
@Table(
        name = "tblroles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_rol_nombre", columnNames = "nombreRol")
        }
)
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Rol extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDrol")
    @EqualsAndHashCode.Include
    private Long idRol;

    @Column(name = "nombreRol", nullable = false, length = 60)
    private String nombreRol;
}