package com.ep18.couriersync.backend.customers.api.graphql;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageInfo;
import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.customers.dto.DomicilioDTOs.CreateDomicilioInput;
import com.ep18.couriersync.backend.customers.dto.DomicilioDTOs.DomicilioView;
import com.ep18.couriersync.backend.customers.dto.DomicilioDTOs.DetalleLineaInput;
import com.ep18.couriersync.backend.customers.dto.DomicilioDTOs.DetalleLineaUpdateInput;
import com.ep18.couriersync.backend.customers.dto.DomicilioDTOs.UpdateDomicilioInput;
import com.ep18.couriersync.backend.customers.service.DomicilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class DomicilioGraphQL {

    private final DomicilioService service;

    /* ========================= Queries ========================= */

    @QueryMapping
    public DomicilioView domicilioById(@Argument Integer id) {
        return service.findById(id);
    }

    @QueryMapping
    public PageModels.DomicilioPage domiciliosByUsuario(@Argument Integer idUsuario,
                                                        @Argument Integer page,
                                                        @Argument Integer size) {
        PageResponse<DomicilioView> resp = service.listByUsuario(idUsuario, page, size);
        return new PageModels.DomicilioPage(resp.content(), resp.pageInfo());
    }

    @QueryMapping
    public PageModels.DomicilioPage domiciliosByEstado(@Argument String estado,
                                                       @Argument Integer page,
                                                       @Argument Integer size) {
        PageResponse<DomicilioView> resp = service.listByEstado(estado, page, size);
        return new PageModels.DomicilioPage(resp.content(), resp.pageInfo());
    }

    @QueryMapping
    public PageModels.DomicilioPage domiciliosByFecha(@Argument
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                      @Argument
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                                      @Argument Integer page,
                                                      @Argument Integer size) {
        PageResponse<DomicilioView> resp = service.listByFecha(start, end, page, size);
        return new PageModels.DomicilioPage(resp.content(), resp.pageInfo());
    }

    /* ======================== Mutations ======================== */

    @MutationMapping
    public DomicilioView createDomicilio(@Argument CreateDomicilioInput input) {
        return service.create(input);
    }

    @MutationMapping
    public DomicilioView updateDomicilio(@Argument UpdateDomicilioInput input) {
        return service.update(input);
    }

    @MutationMapping
    public Boolean deleteDomicilio(@Argument Integer id) {
        return service.delete(id);
    }
}