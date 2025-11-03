package com.ep18.couriersync.backend.customers.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

/** DTOs de Domicilio + líneas de detalle. */
public final class DomicilioDTOs {
    private DomicilioDTOs() {}

    /* ====== LÍNEAS (DetalleDomicilio) ====== */

    /** Para crear nuevas líneas. */
    public record DetalleLineaInput(
            @NotNull Integer idProducto,
            @NotNull @Min(1) Integer cantidad,
            // Precio neto de la línea (sin costo de envío)
            @NotNull @PositiveOrZero Double precioNeto
    ) {}

    /** Para actualizar líneas existentes. */
    public record DetalleLineaUpdateInput(
            @NotNull Integer idDetalle,
            Integer idProducto,
            @Min(1) Integer cantidad,
            @PositiveOrZero Double precioNeto
    ) {}

    /** Vista de línea. */
    public record DetalleLineaView(
            Integer idDetalle,
            Integer idProducto,
            String  nombreProducto,
            Integer cantidad,
            Double  precioNeto
    ) {}

    /* ====== DOMICILIO ====== */

    public record CreateDomicilioInput(
            @NotNull Integer idUsuario,
            @NotBlank @Size(max = 100) String cedulaRecibe,
            @NotNull LocalDate fechaPedido,
            @NotNull LocalDate fechaEntrega,
            // Si vienen null el service puede calcularlos a partir de los detalles.
            @PositiveOrZero Double valorPedido,
            @PositiveOrZero Double valorDomicilio,
            @PositiveOrZero Double valorTotal,
            @NotBlank @Size(max = 30) String estado,
            @NotEmpty List<DetalleLineaInput> detalles
    ) {}

    public record UpdateDomicilioInput(
            @NotNull Integer idDomicilio,
            Integer idUsuario,                          // opcional (re-asignación)
            @Size(max = 100) String cedulaRecibe,
            LocalDate fechaPedido,
            LocalDate fechaEntrega,
            @PositiveOrZero Double valorPedido,
            @PositiveOrZero Double valorDomicilio,
            @PositiveOrZero Double valorTotal,
            @Size(max = 30) String estado,
            // Estrategia de actualización granular de líneas:
            List<DetalleLineaInput>       addDetalles,     // nuevas
            List<DetalleLineaUpdateInput> updateDetalles,  // modificar existentes
            List<Integer>                 deleteDetalleIds  // eliminar por id
    ) {}

    /** Vista “flattened” para no exponer entidades. */
    public record DomicilioView(
            Integer idDomicilio,
            Integer idUsuario,
            String  nombreUsuario,
            String  cedulaRecibe,
            LocalDate fechaPedido,
            LocalDate fechaEntrega,
            Double valorPedido,
            Double valorDomicilio,
            Double valorTotal,
            String estado,
            List<DetalleLineaView> detalles
    ) {}
}