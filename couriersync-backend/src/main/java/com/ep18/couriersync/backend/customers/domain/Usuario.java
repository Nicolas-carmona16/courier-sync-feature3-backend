package com.ep18.couriersync.backend.customers.domain;

import com.ep18.couriersync.backend.common.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Tabla: public.tblusuarios
 */
@Entity
@Table(
        name = "tblusuarios",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "unicidad_correo", columnNames = "correo")
        }
)
@Getter @Setter
@ToString(exclude = {"ciudad", "departamento", "rol", "contrasena"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Usuario extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idUsuario;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "correo", nullable = false, length = 100)
    private String correo;

    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "detalle_direccion", nullable = false, length = 100)
    private String detalleDireccion;

    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "ciudad",
            nullable = false,
            foreignKey = @ForeignKey(name = "ciudad_usuario")
    )
    private Ciudad ciudad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "departamento",
            nullable = false,
            foreignKey = @ForeignKey(name = "departamento_usuario")
    )
    private Departamento departamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "rol",
            nullable = false,
            foreignKey = @ForeignKey(name = "rol_usuario")
    )
    private Rol rol;
}