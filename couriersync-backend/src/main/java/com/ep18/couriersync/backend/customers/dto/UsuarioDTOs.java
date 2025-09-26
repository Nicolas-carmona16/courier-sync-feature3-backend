package com.ep18.couriersync.backend.customers.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/** DTOs de Usuario (crear/actualizar/ver). */
public final class UsuarioDTOs {
    private UsuarioDTOs() {}

    // Puedes ajustar el patrón de teléfono a tu realidad local si quieres endurecerlo más.
    private static final String PHONE_REGEX = "^[+0-9()\\-\\s]{7,30}$";

    public record CreateUsuarioInput(
            @NotBlank @Size(max = 120) String nombre,
            @NotBlank @Email @Size(max = 180) String correo,
            @NotBlank @Pattern(regexp = PHONE_REGEX, message = "Formato de teléfono inválido") @Size(max = 30) String telefono,
            @NotNull LocalDate fechaRegistro,
            @NotBlank @Size(max = 250) String detalleDireccion,
            @NotNull Long idCiudad,
            @NotNull Long idDepartamento,
            @NotNull Long idRol
    ) {}

    public record UpdateUsuarioInput(
            @NotNull Long idUsuario,
            @Size(max = 120) String nombre,
            @Email @Size(max = 180) String correo,
            @Pattern(regexp = PHONE_REGEX, message = "Formato de teléfono inválido") @Size(max = 30) String telefono,
            LocalDate fechaRegistro,
            @Size(max = 250) String detalleDireccion,
            Long idCiudad,
            Long idDepartamento,
            Long idRol
    ) {}

    /** Vista “flattened” con nombres e IDs para no exponer entidades. */
    public record UsuarioView(
            Long idUsuario,
            String nombre,
            String correo,
            String telefono,
            LocalDate fechaRegistro,
            String detalleDireccion,
            Long idCiudad,
            String nombreCiudad,
            Long idDepartamento,
            String nombreDepartamento,
            Long idRol,
            String nombreRol
    ) {}
}