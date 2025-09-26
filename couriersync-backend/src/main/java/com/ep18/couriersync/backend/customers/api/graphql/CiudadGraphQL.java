package com.ep18.couriersync.backend.customers.api.graphql;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.customers.dto.CiudadDTOs.*;
import com.ep18.couriersync.backend.customers.service.CiudadService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CiudadGraphQL {

    private final CiudadService service;

    @QueryMapping
    public CiudadView ciudadById(@Argument Long id) {
        return service.findById(id);
    }

    @QueryMapping
    public PageModels.CiudadPage ciudadesByDepartamento(@Argument Long idDepartamento,
                                                        @Argument Integer page,
                                                        @Argument Integer size) {
        var resp = service.listByDepartamento(idDepartamento, page, size);
        return new PageModels.CiudadPage(resp.content(), resp.pageInfo());
    }

    @QueryMapping
    public PageModels.CiudadPage searchCiudades(@Argument String q,
                                                @Argument Integer page,
                                                @Argument Integer size) {
        var resp = service.search(q, page, size);
        return new PageModels.CiudadPage(resp.content(), resp.pageInfo());
    }

    @MutationMapping
    public CiudadView createCiudad(@Argument CreateCiudadInput input) {
        return service.create(input);
    }

    @MutationMapping
    public CiudadView updateCiudad(@Argument UpdateCiudadInput input) {
        return service.update(input);
    }

    @MutationMapping
    public Boolean deleteCiudad(@Argument Long id) {
        return service.delete(id);
    }
}