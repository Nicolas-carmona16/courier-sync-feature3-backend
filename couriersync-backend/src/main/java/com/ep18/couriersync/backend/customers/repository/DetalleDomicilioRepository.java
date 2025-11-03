package com.ep18.couriersync.backend.customers.repository;

import com.ep18.couriersync.backend.customers.domain.DetalleDomicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetalleDomicilioRepository extends JpaRepository<DetalleDomicilio, Integer> {
    List<DetalleDomicilio> findAllByDomicilio_IdDomicilio(Integer idDomicilio);
    void deleteAllByDomicilio_IdDomicilioAndIdDetalleIn(Integer idDomicilio, List<Integer> ids);
}