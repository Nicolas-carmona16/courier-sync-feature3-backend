package com.ep18.couriersync.backend.customers.api.graphql;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageInfo;
import java.util.List;

public final class PageModels {
    private PageModels() {}

    public record DepartamentoPage(
            List<com.ep18.couriersync.backend.customers.dto.DepartamentoDTOs.DepartamentoView> content,
            PageInfo pageInfo) {}

    public record CiudadPage(
            List<com.ep18.couriersync.backend.customers.dto.CiudadDTOs.CiudadView> content,
            PageInfo pageInfo) {}

    public record RolPage(
            List<com.ep18.couriersync.backend.customers.dto.RolDTOs.RolView> content,
            PageInfo pageInfo) {}

    public record UsuarioPage(
            List<com.ep18.couriersync.backend.customers.dto.UsuarioDTOs.UsuarioView> content,
            PageInfo pageInfo) {}
}