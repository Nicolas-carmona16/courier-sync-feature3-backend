package com.ep18.couriersync.backend.customers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** DTOs de Rol (crear/actualizar/ver). */
public final class RolDTOs {
    private RolDTOs() {}

    public record CreateRolInput(
            @NotBlank @Size(max = 50) String nombreRol
    ) {}

    public record UpdateRolInput(
            @NotNull Integer idRol,
            @NotBlank @Size(max = 50) String nombreRol
    ) {}

    public record RolView(
            Integer idRol,
            String nombreRol
    ) {}
}