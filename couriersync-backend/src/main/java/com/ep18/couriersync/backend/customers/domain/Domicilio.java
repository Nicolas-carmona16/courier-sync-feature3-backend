package com.ep18.couriersync.backend.customers.domain;

import com.ep18.couriersync.backend.common.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbldomicilios", schema = "public")
@Getter @Setter
@ToString(exclude = {"usuario", "detalles"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Domicilio extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_domicilio", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idDomicilio;

    // FK: usuario_domicilio -> public.tblusuarios(id_usuario)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "usuario_domicilio")
    )
    private Usuario usuario;

    @Column(name = "cedula_recibe", nullable = false, length = 100)
    private String cedulaRecibe;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido;

    @Column(name = "fecha_entrega", nullable = false)
    private LocalDate fechaEntrega;

    // Totales monetarios (REAL en BD)
    @Column(name = "valor_pedido", nullable = false)
    private Double valorPedido;

    @Column(name = "valor_domicilio", nullable = false)
    private Double valorDomicilio;

    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    // Relación 1..N con detalles
    @OneToMany(
            mappedBy = "domicilio",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleDomicilio> detalles = new ArrayList<>();

    /** Utilidad opcional para mantener consistencia desde el dominio */
    public void addDetalle(DetalleDomicilio det) {
        det.setDomicilio(this);
        this.detalles.add(det);
    }

    public void removeDetalle(DetalleDomicilio det) {
        det.setDomicilio(null);
        this.detalles.remove(det);
    }
}