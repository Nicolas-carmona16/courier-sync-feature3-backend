package com.ep18.couriersync.backend.customers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** DTOs de Ciudad (crear/actualizar/ver). */
public final class CiudadDTOs {
    private CiudadDTOs() {}

    public record CreateCiudadInput(
            @NotBlank @Size(max = 30) String nombreCiudad,
            @NotNull Integer idDepartamento
    ) {}

    public record UpdateCiudadInput(
            @NotNull Integer idCiudad,
            @Size(max = 30) String nombreCiudad,
            Integer idDepartamento
    ) {}

    public record CiudadView(
            Integer idCiudad,
            String nombreCiudad,
            Integer idDepartamento,
            String nombreDepartamento
    ) {}
}