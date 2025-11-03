package com.ep18.couriersync.backend.customers.domain;

import com.ep18.couriersync.backend.common.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbldetalledomicilio", schema = "public")
@Getter @Setter
@ToString(exclude = {"domicilio", "producto"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class DetalleDomicilio extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idDetalle;

    // FK: detalle_producto -> public.tblproductos(id_producto)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "producto",
            nullable = false,
            foreignKey = @ForeignKey(name = "detalle_producto")
    )
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    // REAL en BD: precio neto de la línea (sin delivery)
    @Column(name = "precio_neto", nullable = false)
    private Double precioNeto;

    // FK: detalle_domicilio -> public.tbldomicilios(id_domicilio)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_domicilio",
            nullable = false,
            foreignKey = @ForeignKey(name = "detalle_domicilio")
    )
    private Domicilio domicilio;
}