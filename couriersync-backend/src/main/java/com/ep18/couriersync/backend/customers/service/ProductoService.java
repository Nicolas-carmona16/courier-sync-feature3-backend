package com.ep18.couriersync.backend.customers.service;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.common.exception.ConflictException;
import com.ep18.couriersync.backend.common.exception.NotFoundException;
import com.ep18.couriersync.backend.common.pagination.PageMapper;
import com.ep18.couriersync.backend.common.pagination.PageRequestUtil;
import com.ep18.couriersync.backend.customers.domain.Producto;
import com.ep18.couriersync.backend.customers.dto.ProductoDTOs.CreateProductoInput;
import com.ep18.couriersync.backend.customers.dto.ProductoDTOs.ProductoView;
import com.ep18.couriersync.backend.customers.dto.ProductoDTOs.UpdateProductoInput;
import com.ep18.couriersync.backend.customers.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepo;

    @Transactional
    public ProductoView create(CreateProductoInput in) {
        // Regla de unicidad por nombre (si vas a usar nombre+marca, cambia aquí)
        if (productoRepo.existsByNombreProductoIgnoreCase(in.nombreProducto())) {
            throw new ConflictException("Ya existe un producto con ese nombre");
        }

        Producto p = new Producto();
        p.setNombreProducto(in.nombreProducto());
        p.setPrecioUnitario(in.precioUnitario());
        p.setIvaProducto(in.ivaProducto());
        p.setMarca(in.marca());

        return toView(productoRepo.save(p));
    }

    @Transactional
    public ProductoView update(UpdateProductoInput in) {
        Producto p = productoRepo.findById(in.idProducto())
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        // Si cambian el nombre, valida colisión
        if (in.nombreProducto() != null
                && !in.nombreProducto().equalsIgnoreCase(p.getNombreProducto())
                && productoRepo.existsByNombreProductoIgnoreCase(in.nombreProducto())) {
            throw new ConflictException("Ya existe un producto con ese nombre");
        }

        if (in.nombreProducto() != null)  p.setNombreProducto(in.nombreProducto());
        if (in.precioUnitario() != null)  p.setPrecioUnitario(in.precioUnitario());
        if (in.ivaProducto() != null)     p.setIvaProducto(in.ivaProducto());
        if (in.marca() != null)           p.setMarca(in.marca());

        return toView(productoRepo.save(p));
    }

    @Transactional(readOnly = true)
    public ProductoView findById(Integer id) {
        return productoRepo.findById(id)
                .map(this::toView)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductoView> search(String q, Integer page, Integer size) {
        Page<Producto> p = productoRepo.findByNombreProductoContainingIgnoreCase(
                (q == null ? "" : q),
                PageRequestUtil.of(page, size, Sort.by("nombreProducto").ascending())
        );
        return PageMapper.map(p, this::toView);
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!productoRepo.existsById(id)) return false;
        try {
            productoRepo.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            // Si hay FK desde DetalleDomicilio, levantará error: conviértelo en 409
            throw new ConflictException("No se puede eliminar: el producto tiene movimientos asociados");
        }
    }

    private ProductoView toView(Producto p) {
        return new ProductoView(
                p.getIdProducto(),
                p.getNombreProducto(),
                p.getPrecioUnitario(),
                p.getIvaProducto(),
                p.getMarca()
        );
    }
}