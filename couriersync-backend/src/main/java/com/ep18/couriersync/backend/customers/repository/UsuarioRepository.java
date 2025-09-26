package com.ep18.couriersync.backend.customers.repository;

import com.ep18.couriersync.backend.customers.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoIgnoreCase(String correo);
    boolean existsByCorreoIgnoreCase(String correo);

    Optional<Usuario> findByTelefono(String telefono);
    boolean existsByTelefono(String telefono);

    /** Búsqueda por nombre (paginada) */
    Page<Usuario> findByNombreContainingIgnoreCase(String q, Pageable pageable);

    /** Filtros por FKs (paginados) */
    Page<Usuario> findAllByCiudad_IdCiudad(Long idCiudad, Pageable pageable);
    Page<Usuario> findAllByDepartamento_IdDepartamento(Long idDepartamento, Pageable pageable);
    Page<Usuario> findAllByRol_IdRol(Long idRol, Pageable pageable);
}