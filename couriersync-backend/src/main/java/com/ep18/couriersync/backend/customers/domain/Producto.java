package com.ep18.couriersync.backend.customers.domain;

import com.ep18.couriersync.backend.common.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tblproductos", schema = "public")
@Getter @Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Producto extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idProducto;

    @Column(name = "nombre_producto", nullable = false, length = 100)
    private String nombreProducto;

    // REAL en BD
    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    // REAL en BD (ej. 0.19 para 19%)
    @Column(name = "iva_producto", nullable = false)
    private Double ivaProducto;

    @Column(name = "marca", nullable = false, length = 100)
    private String marca;
}