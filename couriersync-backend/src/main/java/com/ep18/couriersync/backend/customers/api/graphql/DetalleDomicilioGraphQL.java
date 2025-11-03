package com.ep18.couriersync.backend.customers.api.graphql;

import com.ep18.couriersync.backend.customers.domain.DetalleDomicilio;
import com.ep18.couriersync.backend.customers.dto.DomicilioDTOs.DetalleLineaView;
import com.ep18.couriersync.backend.customers.repository.DetalleDomicilioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DetalleDomicilioGraphQL {

    private final DetalleDomicilioRepository repo;

    @QueryMapping
    public List<DetalleLineaView> detallesByDomicilio(@Argument Integer idDomicilio) {
        List<DetalleDomicilio> list = repo.findAllByDomicilio_IdDomicilio(idDomicilio);
        return list.stream()
                .map(d -> new DetalleLineaView(
                        d.getIdDetalle(),
                        d.getProducto().getIdProducto(),
                        d.getProducto().getNombreProducto(),
                        d.getCantidad(),
                        d.getPrecioNeto()
                ))
                .toList();
    }
}