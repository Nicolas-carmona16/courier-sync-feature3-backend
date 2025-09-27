package com.ep18.couriersync.backend.customers.repository;

import com.ep18.couriersync.backend.customers.domain.Ciudad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {

    /** Unicidad lógica: nombreCiudad dentro de un mismo departamento */
    Optional<Ciudad> findByNombreCiudadIgnoreCaseAndDepartamento_IdDepartamento(
            String nombreCiudad, Integer idDepartamento);

    boolean existsByNombreCiudadIgnoreCaseAndDepartamento_IdDepartamento(
            String nombreCiudad, Integer idDepartamento);

    /** Listado por departamento (paginado) */
    Page<Ciudad> findAllByDepartamento_IdDepartamento(Integer idDepartamento, Pageable pageable);

    /** Búsqueda por nombre (paginada) */
    Page<Ciudad> findByNombreCiudadContainingIgnoreCase(String q, Pageable pageable);
}