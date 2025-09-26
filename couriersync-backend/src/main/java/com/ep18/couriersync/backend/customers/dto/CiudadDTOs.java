package com.ep18.couriersync.backend.customers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** DTOs de Ciudad (crear/actualizar/ver). */
public final class CiudadDTOs {
    private CiudadDTOs() {}

    public record CreateCiudadInput(
            @NotBlank @Size(max = 150) String nombreCiudad,
            @NotNull Long idDepartamento
    ) {}

    public record UpdateCiudadInput(
            @NotNull Long idCiudad,
            @Size(max = 150) String nombreCiudad,
            Long idDepartamento
    ) {}

    public record CiudadView(
            Long idCiudad,
            String nombreCiudad,
            Long idDepartamento,
            String nombreDepartamento
    ) {}
}