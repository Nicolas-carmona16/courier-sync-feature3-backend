package com.ep18.couriersync.backend.customers.domain;

import com.ep18.couriersync.backend.common.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * tblusuarios
 * PK: IDusuario
 * FKs:ciudad, departamento, rol
 * - Unicidad práctica en correo y teléfono.
 * - Índices para FKs.
 */
@Entity
@Table(
        name = "tblusuarios",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_usuario_correo", columnNames = "correo"),
                @UniqueConstraint(name = "uk_usuario_telefono", columnNames = "telefono")
        },
        indexes = {
                @Index(name = "idx_usuario_ciudad", columnList = "ciudad"),
                @Index(name = "idx_usuario_departamento", columnList = "departamento"),
                @Index(name = "idx_usuario_rol", columnList = "rol")
        }
)
@Getter
@Setter
@ToString(exclude = {"ciudad", "departamento", "rol"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Usuario extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDusuario")
    @EqualsAndHashCode.Include
    private Long idUsuario;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "correo", nullable = false, length = 180)
    private String correo;

    @Column(name = "telefono", nullable = false, length = 30)
    private String telefono;

    @Column(name = "fechaRegistro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "detalleDireccion", nullable = false, length = 250)
    private String detalleDireccion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "ciudad",                 // en tu DB la columna FK se llama así
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario_ciudad")
    )
    private Ciudad ciudad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "departamento",           // en tu DB la columna FK se llama así
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario_departamento")
    )
    private Departamento departamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "rol",                    // en tu DB la columna FK se llama así
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario_rol")
    )
    private Rol rol;
}