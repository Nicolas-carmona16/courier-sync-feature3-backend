package com.ep18.couriersync.backend.customers.repository;

import com.ep18.couriersync.backend.customers.domain.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    boolean existsByNombreProductoIgnoreCase(String nombre);
    Page<Producto> findByNombreProductoContainingIgnoreCase(String q, Pageable pageable);
}