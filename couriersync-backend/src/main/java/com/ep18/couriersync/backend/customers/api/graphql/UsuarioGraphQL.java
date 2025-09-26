package com.ep18.couriersync.backend.customers.api.graphql;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.customers.dto.UsuarioDTOs.*;
import com.ep18.couriersync.backend.customers.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UsuarioGraphQL {

    private final UsuarioService service;

    @QueryMapping
    public UsuarioView usuarioById(@Argument Long id) {
        return service.findById(id);
    }

    @QueryMapping
    public PageModels.UsuarioPage searchUsuarios(@Argument String q,
                                                 @Argument Integer page,
                                                 @Argument Integer size) {
        PageResponse<UsuarioView> resp = service.search(q, page, size);
        return new PageModels.UsuarioPage(resp.content(), resp.pageInfo());
    }

    @QueryMapping
    public PageModels.UsuarioPage usuariosByCiudad(@Argument Long idCiudad,
                                                   @Argument Integer page,
                                                   @Argument Integer size) {
        var resp = service.listByCiudad(idCiudad, page, size);
        return new PageModels.UsuarioPage(resp.content(), resp.pageInfo());
    }

    @QueryMapping
    public PageModels.UsuarioPage usuariosByDepartamento(@Argument Long idDepartamento,
                                                         @Argument Integer page,
                                                         @Argument Integer size) {
        var resp = service.listByDepartamento(idDepartamento, page, size);
        return new PageModels.UsuarioPage(resp.content(), resp.pageInfo());
    }

    @QueryMapping
    public PageModels.UsuarioPage usuariosByRol(@Argument Long idRol,
                                                @Argument Integer page,
                                                @Argument Integer size) {
        var resp = service.listByRol(idRol, page, size);
        return new PageModels.UsuarioPage(resp.content(), resp.pageInfo());
    }

    @MutationMapping
    public UsuarioView createUsuario(@Argument CreateUsuarioInput input) {
        return service.create(input);
    }

    @MutationMapping
    public UsuarioView updateUsuario(@Argument UpdateUsuarioInput input) {
        return service.update(input);
    }

    @MutationMapping
    public Boolean deleteUsuario(@Argument Long id) {
        return service.delete(id);
    }
}