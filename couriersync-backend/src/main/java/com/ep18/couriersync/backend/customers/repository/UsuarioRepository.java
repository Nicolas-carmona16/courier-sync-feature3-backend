package com.ep18.couriersync.backend.customers.repository;

import com.ep18.couriersync.backend.customers.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreoIgnoreCase(String correo);
    boolean existsByCorreoIgnoreCase(String correo);

    /** Búsqueda por nombre (paginada) */
    Page<Usuario> findByNombreContainingIgnoreCase(String q, Pageable pageable);

    /** Filtros por FKs (paginados) */
    Page<Usuario> findAllByCiudad_IdCiudad(Integer idCiudad, Pageable pageable);
    Page<Usuario> findAllByDepartamento_IdDepartamento(Integer idDepartamento, Pageable pageable);
    Page<Usuario> findAllByRol_IdRol(Integer idRol, Pageable pageable);
}