package com.ep18.couriersync.backend.customers.repository;

import com.ep18.couriersync.backend.customers.domain.Ciudad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    /** Unicidad: nombreCiudad dentro de un mismo departamento */
    Optional<Ciudad> findByNombreCiudadIgnoreCaseAndDepartamento_IdDepartamento(
            String nombreCiudad, Long idDepartamento);

    boolean existsByNombreCiudadIgnoreCaseAndDepartamento_IdDepartamento(
            String nombreCiudad, Long idDepartamento);

    /** Listado por departamento (paginado) */
    Page<Ciudad> findAllByDepartamento_IdDepartamento(Long idDepartamento, Pageable pageable);

    /** Búsqueda por nombre (paginada) */
    Page<Ciudad> findByNombreCiudadContainingIgnoreCase(String q, Pageable pageable);
}