package com.ep18.couriersync.backend.customers.repository;

import com.ep18.couriersync.backend.customers.domain.Domicilio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface DomicilioRepository extends JpaRepository<Domicilio, Integer> {

    /** Carga el agregado completo usando la PK estándar */
    @Override
    @EntityGraph(attributePaths = {"usuario", "detalles", "detalles.producto"})
    Optional<Domicilio> findById(Integer id);

    Page<Domicilio> findAllByUsuario_IdUsuario(Integer idUsuario, Pageable pageable);
    Page<Domicilio> findAllByEstadoIgnoreCase(String estado, Pageable pageable);
    Page<Domicilio> findAllByFechaPedidoBetween(LocalDate start, LocalDate end, Pageable pageable);
}
