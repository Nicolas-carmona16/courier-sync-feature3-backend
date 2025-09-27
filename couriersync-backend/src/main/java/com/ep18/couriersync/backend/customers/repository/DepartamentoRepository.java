package com.ep18.couriersync.backend.customers.repository;

import com.ep18.couriersync.backend.customers.domain.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {

    Optional<Departamento> findByNombreDepartamentoIgnoreCase(String nombreDepartamento);

    boolean existsByNombreDepartamentoIgnoreCase(String nombreDepartamento);
}