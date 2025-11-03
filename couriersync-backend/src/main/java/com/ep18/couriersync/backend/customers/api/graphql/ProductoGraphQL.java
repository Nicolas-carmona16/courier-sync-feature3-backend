package com.ep18.couriersync.backend.customers.api.graphql;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.customers.dto.ProductoDTOs.CreateProductoInput;
import com.ep18.couriersync.backend.customers.dto.ProductoDTOs.ProductoView;
import com.ep18.couriersync.backend.customers.dto.ProductoDTOs.UpdateProductoInput;
import com.ep18.couriersync.backend.customers.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProductoGraphQL {

    private final ProductoService service;

    @QueryMapping
    public ProductoView productoById(@Argument Integer id) {
        return service.findById(id);
    }

    @QueryMapping
    public PageModels.ProductoPage searchProductos(@Argument String q,
                                                   @Argument Integer page,
                                                   @Argument Integer size) {
        PageResponse<ProductoView> resp = service.search(q, page, size);
        return new PageModels.ProductoPage(resp.content(), resp.pageInfo());
    }

    @MutationMapping
    public ProductoView createProducto(@Argument CreateProductoInput input) {
        return service.create(input);
    }

    @MutationMapping
    public ProductoView updateProducto(@Argument UpdateProductoInput input) {
        return service.update(input);
    }

    @MutationMapping
    public Boolean deleteProducto(@Argument Integer id) {
        return service.delete(id);
    }
}
