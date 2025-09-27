package com.ep18.couriersync.backend.customers.api.graphql;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.customers.dto.UsuarioDTOs.CreateUsuarioInput;
import com.ep18.couriersync.backend.customers.dto.UsuarioDTOs.UpdateUsuarioInput;
import com.ep18.couriersync.backend.customers.dto.UsuarioDTOs.UsuarioView;
import com.ep18.couriersync.backend.customers.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@Validated
@RequiredArgsConstructor
public class UsuarioGraphQL {

    private final UsuarioService service;

    @QueryMapping
    public UsuarioView usuarioById(@Argument Integer id) {
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
    public PageModels.UsuarioPage usuariosByCiudad(@Argument Integer idCiudad,
                                                   @Argument Integer page,
                                                   @Argument Integer size) {
        var resp = service.listByCiudad(idCiudad, page, size);
        return new PageModels.UsuarioPage(resp.content(), resp.pageInfo());
    }

    @QueryMapping
    public PageModels.UsuarioPage usuariosByDepartamento(@Argument Integer idDepartamento,
                                                         @Argument Integer page,
                                                         @Argument Integer size) {
        var resp = service.listByDepartamento(idDepartamento, page, size);
        return new PageModels.UsuarioPage(resp.content(), resp.pageInfo());
    }

    @QueryMapping
    public PageModels.UsuarioPage usuariosByRol(@Argument Integer idRol,
                                                @Argument Integer page,
                                                @Argument Integer size) {
        var resp = service.listByRol(idRol, page, size);
        return new PageModels.UsuarioPage(resp.content(), resp.pageInfo());
    }

    @MutationMapping
    public UsuarioView createUsuario(@Argument @Valid CreateUsuarioInput input) {
        return service.create(input);
    }

    @MutationMapping
    public UsuarioView updateUsuario(@Argument @Valid UpdateUsuarioInput input) {
        return service.update(input);
    }

    @MutationMapping
    public Boolean deleteUsuario(@Argument Integer id) {
        return service.delete(id);
    }
}