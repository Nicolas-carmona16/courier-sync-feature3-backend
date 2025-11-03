package com.ep18.couriersync.backend.customers.service;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.common.exception.ConflictException;
import com.ep18.couriersync.backend.common.exception.NotFoundException;
import com.ep18.couriersync.backend.common.pagination.PageMapper;
import com.ep18.couriersync.backend.common.pagination.PageRequestUtil;
import com.ep18.couriersync.backend.customers.domain.*;
import com.ep18.couriersync.backend.customers.dto.DomicilioDTOs.*;
import com.ep18.couriersync.backend.customers.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DomicilioService {

    private final DomicilioRepository domicilioRepo;
    private final DetalleDomicilioRepository detalleRepo;
    private final UsuarioRepository usuarioRepo;
    private final ProductoRepository productoRepo;

    /* ===================== CRUD agregado ===================== */

    @Transactional
    public DomicilioView create(CreateDomicilioInput in) {
        Usuario usuario = usuarioRepo.findById(in.idUsuario())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Domicilio dom = new Domicilio();
        dom.setUsuario(usuario);
        dom.setCedulaRecibe(in.cedulaRecibe());
        dom.setFechaPedido(in.fechaPedido() != null ? in.fechaPedido() : LocalDate.now());
        dom.setFechaEntrega(in.fechaEntrega());
        dom.setEstado(in.estado() != null ? in.estado() : "CREADO");
        dom.setValorDomicilio(nvl(in.valorDomicilio()));

        // Añadir líneas
        List<DetalleDomicilio> lineas = new ArrayList<>();
        for (DetalleLineaInput li : in.detalles()) {
            Producto p = productoRepo.findById(li.idProducto())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + li.idProducto()));

            DetalleDomicilio d = new DetalleDomicilio();
            d.setProducto(p);
            d.setCantidad(li.cantidad());
            d.setPrecioNeto(li.precioNeto());
            d.setDomicilio(dom);

            lineas.add(d);
        }
        dom.setDetalles(lineas);

        // Recalcular totales
        recalcTotales(dom);

        // Persistir agregado
        Domicilio saved = domicilioRepo.save(dom);
        return toView(fetchAgregado(saved.getIdDomicilio()));
    }

    @Transactional
    public DomicilioView update(UpdateDomicilioInput in) {
        Domicilio dom = domicilioRepo.findById(in.idDomicilio())
                .orElseThrow(() -> new NotFoundException("Domicilio no encontrado"));

        // Reglas de negocio (ej.: no editar si ENTREGADO/CANCELADO)
        if (isEstadoCerrado(dom.getEstado())) {
            throw new ConflictException("El domicilio no es editable en estado: " + dom.getEstado());
        }

        // Cambios simples
        if (in.cedulaRecibe() != null) dom.setCedulaRecibe(in.cedulaRecibe());
        if (in.fechaPedido() != null)   dom.setFechaPedido(in.fechaPedido());
        if (in.fechaEntrega() != null)  dom.setFechaEntrega(in.fechaEntrega());
        if (in.estado() != null)        dom.setEstado(in.estado());
        if (in.valorDomicilio() != null) dom.setValorDomicilio(nvl(in.valorDomicilio()));

        // Manejo granular de líneas
        if (in.addDetalles() != null) {
            for (DetalleLineaInput li : in.addDetalles()) {
                Producto p = productoRepo.findById(li.idProducto())
                        .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + li.idProducto()));
                DetalleDomicilio d = new DetalleDomicilio();
                d.setProducto(p);
                d.setCantidad(li.cantidad());
                d.setPrecioNeto(li.precioNeto());
                d.setDomicilio(dom);
                dom.getDetalles().add(d);
            }
        }

        if (in.updateDetalles() != null) {
            for (DetalleLineaUpdateInput li : in.updateDetalles()) {
                DetalleDomicilio d = dom.getDetalles().stream()
                        .filter(x -> x.getIdDetalle().equals(li.idDetalle()))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException("Detalle no encontrado: " + li.idDetalle()));

                if (li.idProducto() != null) {
                    Producto p = productoRepo.findById(li.idProducto())
                            .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + li.idProducto()));
                    d.setProducto(p);
                }
                if (li.cantidad() != null)    d.setCantidad(li.cantidad());
                if (li.precioNeto() != null)  d.setPrecioNeto(li.precioNeto());
            }
        }

        if (in.deleteDetalleIds() != null && !in.deleteDetalleIds().isEmpty()) {
            // elimina de la colección en memoria
            dom.getDetalles().removeIf(d -> in.deleteDetalleIds().contains(d.getIdDetalle()));
            // y asegúrate de borrarlos en BD (por si no hay orphanRemoval)
            detalleRepo.deleteAllByDomicilio_IdDomicilioAndIdDetalleIn(dom.getIdDomicilio(), in.deleteDetalleIds());
        }

        // Recalcular totales
        recalcTotales(dom);

        Domicilio saved = domicilioRepo.save(dom);
        return toView(fetchAgregado(saved.getIdDomicilio()));
    }

    @Transactional(readOnly = true)
    public DomicilioView findById(Integer id) {
        return toView(fetchAgregado(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<DomicilioView> listByUsuario(Integer idUsuario, Integer page, Integer size) {
        Page<Domicilio> p = domicilioRepo.findAllByUsuario_IdUsuario(
                idUsuario, PageRequestUtil.of(page, size, Sort.by("fechaPedido").descending()));
        return PageMapper.map(p, this::toViewLight); // versión light para listar
    }

    @Transactional(readOnly = true)
    public PageResponse<DomicilioView> listByEstado(String estado, Integer page, Integer size) {
        Page<Domicilio> p = domicilioRepo.findAllByEstadoIgnoreCase(
                estado, PageRequestUtil.of(page, size, Sort.by("fechaPedido").descending()));
        return PageMapper.map(p, this::toViewLight);
    }

    @Transactional(readOnly = true)
    public PageResponse<DomicilioView> listByFecha(LocalDate start, LocalDate end, Integer page, Integer size) {
        Page<Domicilio> p = domicilioRepo.findAllByFechaPedidoBetween(
                start, end, PageRequestUtil.of(page, size, Sort.by("fechaPedido").descending()));
        return PageMapper.map(p, this::toViewLight);
    }

    @Transactional
    public boolean delete(Integer id) {
        Domicilio dom = domicilioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Domicilio no encontrado"));
        if (isEstadoCerrado(dom.getEstado())) {
            throw new ConflictException("No se puede eliminar un domicilio en estado: " + dom.getEstado());
        }
        domicilioRepo.delete(dom);
        return true;
    }

    /* ===================== Helpers ===================== */

    private Domicilio fetchAgregado(Integer id) {
        return domicilioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Domicilio no encontrado"));
    }

    private void recalcTotales(Domicilio dom) {
        double valorPedido = dom.getDetalles() == null ? 0.0 :
                dom.getDetalles().stream()
                        .mapToDouble(d -> nvl(d.getPrecioNeto()) * nvl(d.getCantidad()))
                        .sum();
        dom.setValorPedido(valorPedido);
        dom.setValorTotal(valorPedido + nvl(dom.getValorDomicilio()));
    }

    private boolean isEstadoCerrado(String estado) {
        if (estado == null) return false;
        String s = estado.trim().toUpperCase();
        return s.equals("ENTREGADO") || s.equals("CANCELADO");
    }

    private double nvl(Double d) { return d == null ? 0.0 : d; }
    private int nvl(Integer i) { return i == null ? 0 : i; }

    /* ===================== Mappers ===================== */

    private DomicilioView toView(Domicilio d) {
        List<DetalleLineaView> dets = d.getDetalles().stream()
                .map(this::toLineaView)
                .toList();
        return new DomicilioView(
                d.getIdDomicilio(),
                d.getUsuario().getIdUsuario(),
                d.getUsuario().getNombre(),
                d.getCedulaRecibe(),
                d.getFechaPedido(),
                d.getFechaEntrega(),
                d.getValorPedido(),
                d.getValorDomicilio(),
                d.getValorTotal(),
                d.getEstado(),
                dets
        );
    }

    /** Para listados rápidos (opcional: puedes devolver sin detalles para reducir payload). */
    private DomicilioView toViewLight(Domicilio d) {
        return new DomicilioView(
                d.getIdDomicilio(),
                d.getUsuario().getIdUsuario(),
                d.getUsuario().getNombre(),
                d.getCedulaRecibe(),
                d.getFechaPedido(),
                d.getFechaEntrega(),
                d.getValorPedido(),
                d.getValorDomicilio(),
                d.getValorTotal(),
                d.getEstado(),
                List.of() // sin detalles para listados
        );
    }

    private DetalleLineaView toLineaView(DetalleDomicilio x) {
        return new DetalleLineaView(
                x.getIdDetalle(),
                x.getProducto().getIdProducto(),
                x.getProducto().getNombreProducto(),
                x.getCantidad(),
                x.getPrecioNeto()
        );
    }
}