package com.ep18.couriersync.backend.customers.repository;

import com.ep18.couriersync.backend.customers.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombreRolIgnoreCase(String nombreRol);

    boolean existsByNombreRolIgnoreCase(String nombreRol);
}