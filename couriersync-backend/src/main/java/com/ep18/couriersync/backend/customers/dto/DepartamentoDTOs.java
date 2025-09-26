package com.ep18.couriersync.backend.customers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** DTOs de Departamento (crear/actualizar/ver). */
public final class DepartamentoDTOs {
    private DepartamentoDTOs() {}

    public record CreateDepartamentoInput(
            @NotBlank @Size(max = 120) String nombreDepartamento
    ) {}

    public record UpdateDepartamentoInput(
            @NotNull Long idDepartamento,
            @NotBlank @Size(max = 120) String nombreDepartamento
    ) {}

    /** Vista de salida “plana” para API/GraphQL. */
    public record DepartamentoView(
            Long idDepartamento,
            String nombreDepartamento
    ) {}
}