package com.ep18.couriersync.backend.customers.service;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.common.exception.ConflictException;
import com.ep18.couriersync.backend.common.exception.NotFoundException;
import com.ep18.couriersync.backend.common.pagination.PageMapper;
import com.ep18.couriersync.backend.common.pagination.PageRequestUtil;
import com.ep18.couriersync.backend.customers.domain.Departamento;
import com.ep18.couriersync.backend.customers.dto.DepartamentoDTOs.CreateDepartamentoInput;
import com.ep18.couriersync.backend.customers.dto.DepartamentoDTOs.DepartamentoView;
import com.ep18.couriersync.backend.customers.dto.DepartamentoDTOs.UpdateDepartamentoInput;
import com.ep18.couriersync.backend.customers.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepo;

    @Transactional
    public DepartamentoView create(CreateDepartamentoInput in) {
        if (departamentoRepo.existsByNombreDepartamentoIgnoreCase(in.nombreDepartamento())) {
            throw new ConflictException("Ya existe un departamento con ese nombre");
        }
        var d = new Departamento();
        d.setNombreDepartamento(in.nombreDepartamento());
        return toView(departamentoRepo.save(d));
    }

    @Transactional
    public DepartamentoView update(UpdateDepartamentoInput in) {
        var d = departamentoRepo.findById(in.idDepartamento())
                .orElseThrow(() -> new NotFoundException("Departamento no encontrado"));

        if (in.nombreDepartamento()!=null &&
                !in.nombreDepartamento().equalsIgnoreCase(d.getNombreDepartamento()) &&
                departamentoRepo.existsByNombreDepartamentoIgnoreCase(in.nombreDepartamento())) {
            throw new ConflictException("Ya existe un departamento con ese nombre");
        }
        if (in.nombreDepartamento()!=null) {
            d.setNombreDepartamento(in.nombreDepartamento());
        }
        return toView(departamentoRepo.save(d));
    }

    @Transactional(readOnly = true)
    public DepartamentoView findById(Integer id) {
        return departamentoRepo.findById(id)
                .map(this::toView)
                .orElseThrow(() -> new NotFoundException("Departamento no encontrado"));
    }

    @Transactional(readOnly = true)
    public PageResponse<DepartamentoView> list(Integer page, Integer size) {
        Page<Departamento> p = departamentoRepo.findAll(
                PageRequestUtil.of(page, size, Sort.by("nombreDepartamento").ascending()));
        return PageMapper.map(p, this::toView);
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!departamentoRepo.existsById(id)) return false;
        try {
            departamentoRepo.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("No se puede eliminar: existen registros relacionados");
        }
    }

    private DepartamentoView toView(Departamento d) {
        return new DepartamentoView(d.getIdDepartamento(), d.getNombreDepartamento());
    }
}