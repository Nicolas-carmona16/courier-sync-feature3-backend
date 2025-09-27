package com.ep18.couriersync.backend.customers.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/** DTOs de Usuario (crear/actualizar/ver). */
public final class UsuarioDTOs {
    private UsuarioDTOs() {}

    // Teléfono exacto 10 dígitos
    private static final String PHONE_REGEX = "^\\d{10}$";

    // Contraseña fuerte: 8+, 1 mayúscula, 1 número, 1 símbolo
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";

    public record CreateUsuarioInput(
            @NotBlank @Size(max = 50)  String nombre,
            @NotBlank @Email @Size(max = 100) String correo,
            @NotBlank @Pattern(regexp = PHONE_REGEX, message = "Debe ser un número de 10 dígitos") String telefono,
            // si viene null el service usará LocalDate.now()
            LocalDate fechaRegistro,
            @NotBlank @Size(max = 100) String detalleDireccion,
            @NotNull Integer idCiudad,
            @NotNull Integer idDepartamento,
            @NotNull Integer idRol,
            @NotBlank
            @Size(min = 8, max = 100)
            @Pattern(regexp = PASSWORD_REGEX,
                    message = "La contraseña debe tener al menos 8 caracteres, 1 mayúscula, 1 número y 1 símbolo")
            String contrasena
    ) {}

    public record UpdateUsuarioInput(
            @NotNull Integer idUsuario,
            @Size(max = 50) String nombre,
            @Email @Size(max = 100) String correo,
            @Pattern(regexp = PHONE_REGEX, message = "Debe ser un número de 10 dígitos") String telefono,
            LocalDate fechaRegistro,
            @Size(max = 100) String detalleDireccion,
            Integer idCiudad,
            Integer idDepartamento,
            Integer idRol,
            @Size(min = 8, max = 100)
            @Pattern(regexp = PASSWORD_REGEX,
                    message = "La contraseña debe tener al menos 8 caracteres, 1 mayúscula, 1 número y 1 símbolo")
            String contrasena
    ) {}

    /** Vista “flattened” con nombres e IDs para no exponer entidades. */
    public record UsuarioView(
            Integer idUsuario,
            String nombre,
            String correo,
            String telefono,
            LocalDate fechaRegistro,
            String detalleDireccion,
            Integer idCiudad,
            String nombreCiudad,
            Integer idDepartamento,
            String nombreDepartamento,
            Integer idRol,
            String nombreRol
    ) {}
}