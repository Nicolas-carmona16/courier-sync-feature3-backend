package com.ep18.couriersync.backend.customers.api.graphql;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.customers.dto.RolDTOs.CreateRolInput;
import com.ep18.couriersync.backend.customers.dto.RolDTOs.RolView;
import com.ep18.couriersync.backend.customers.dto.RolDTOs.UpdateRolInput;
import com.ep18.couriersync.backend.customers.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RolGraphQL {

    private final RolService service;

    @QueryMapping
    public RolView rolById(@Argument Integer id) {
        return service.findById(id);
    }

    @QueryMapping
    public PageModels.RolPage roles(@Argument Integer page, @Argument Integer size) {
        PageResponse<RolView> resp = service.list(page, size);
        return new PageModels.RolPage(resp.content(), resp.pageInfo());
    }

    @MutationMapping
    public RolView createRol(@Argument CreateRolInput input) {
        return service.create(input);
    }

    @MutationMapping
    public RolView updateRol(@Argument UpdateRolInput input) {
        return service.update(input);
    }

    @MutationMapping
    public Boolean deleteRol(@Argument Integer id) {
        return service.delete(id);
    }
}
