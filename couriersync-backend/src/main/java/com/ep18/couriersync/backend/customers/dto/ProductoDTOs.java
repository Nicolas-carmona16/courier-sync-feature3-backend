package com.ep18.couriersync.backend.customers.dto;

import jakarta.validation.constraints.*;

/** DTOs de Producto (crear/actualizar/ver). */
public final class ProductoDTOs {
    private ProductoDTOs() {}

    public record CreateProductoInput(
            @NotBlank @Size(max = 100) String nombreProducto,
            @NotNull  @PositiveOrZero     Double precioUnitario,
            // IVA en fracción (p. ej. 0.19). Ajusta límites si manejas 0..100.
            @NotNull  @DecimalMin("0.0") @DecimalMax("1.0") Double ivaProducto,
            @NotBlank @Size(max = 100) String marca
    ) {}

    public record UpdateProductoInput(
            @NotNull Integer idProducto,
            @Size(max = 100) String nombreProducto,
            @PositiveOrZero  Double precioUnitario,
            @DecimalMin("0.0") @DecimalMax("1.0") Double ivaProducto,
            @Size(max = 100) String marca
    ) {}

    public record ProductoView(
            Integer idProducto,
            String  nombreProducto,
            Double  precioUnitario,
            Double  ivaProducto,
            String  marca
    ) {}
}