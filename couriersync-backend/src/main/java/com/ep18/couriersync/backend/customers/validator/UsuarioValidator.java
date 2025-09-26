package com.ep18.couriersync.backend.customers.validator;

import com.ep18.couriersync.backend.common.exception.ValidationException;
import com.ep18.couriersync.backend.customers.domain.Ciudad;

/** Reglas de coherencia del agregado Usuario. */
public final class UsuarioValidator {
    private UsuarioValidator() {}

    /** Valida que la Ciudad pertenezca al Departamento indicado. */
    public static void assertCiudadPerteneceADepartamento(Ciudad ciudad, Long idDepartamento) {
        Long idDeptoCiudad = ciudad.getDepartamento().getIdDepartamento();
        if (!idDeptoCiudad.equals(idDepartamento)) {
            throw new ValidationException("La ciudad no pertenece al departamento indicado");
        }
    }
}