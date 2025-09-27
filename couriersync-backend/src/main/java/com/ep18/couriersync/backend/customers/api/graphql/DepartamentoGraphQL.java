package com.ep18.couriersync.backend.customers.api.graphql;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.customers.dto.DepartamentoDTOs.CreateDepartamentoInput;
import com.ep18.couriersync.backend.customers.dto.DepartamentoDTOs.DepartamentoView;
import com.ep18.couriersync.backend.customers.dto.DepartamentoDTOs.UpdateDepartamentoInput;
import com.ep18.couriersync.backend.customers.service.DepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DepartamentoGraphQL {

    private final DepartamentoService service;

    @QueryMapping
    public DepartamentoView departamentoById(@Argument Integer id) {
        return service.findById(id);
    }

    @QueryMapping
    public PageModels.DepartamentoPage departamentos(@Argument Integer page, @Argument Integer size) {
        PageResponse<DepartamentoView> resp = service.list(page, size);
        return new PageModels.DepartamentoPage(resp.content(), resp.pageInfo());
    }

    @MutationMapping
    public DepartamentoView createDepartamento(@Argument CreateDepartamentoInput input) {
        return service.create(input);
    }

    @MutationMapping
    public DepartamentoView updateDepartamento(@Argument UpdateDepartamentoInput input) {
        return service.update(input);
    }

    @MutationMapping
    public Boolean deleteDepartamento(@Argument Integer id) {
        return service.delete(id);
    }
}
