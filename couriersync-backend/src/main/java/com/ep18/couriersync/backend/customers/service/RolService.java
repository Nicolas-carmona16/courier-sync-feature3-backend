package com.ep18.couriersync.backend.customers.service;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.common.pagination.PageMapper;
import com.ep18.couriersync.backend.common.pagination.PageRequestUtil;
import com.ep18.couriersync.backend.common.exception.ConflictException;
import com.ep18.couriersync.backend.common.exception.NotFoundException;
import com.ep18.couriersync.backend.customers.domain.Rol;
import com.ep18.couriersync.backend.customers.dto.RolDTOs.CreateRolInput;
import com.ep18.couriersync.backend.customers.dto.RolDTOs.RolView;
import com.ep18.couriersync.backend.customers.dto.RolDTOs.UpdateRolInput;
import com.ep18.couriersync.backend.customers.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepo;

    @Transactional
    public RolView create(CreateRolInput in) {
        if (rolRepo.existsByNombreRolIgnoreCase(in.nombreRol())) {
            throw new ConflictException("Ya existe un rol con ese nombre");
        }
        Rol r = new Rol();
        r.setNombreRol(in.nombreRol());
        return toView(rolRepo.save(r));
    }

    @Transactional
    public RolView update(UpdateRolInput in) {
        Rol r = rolRepo.findById(in.idRol())
                .orElseThrow(() -> new NotFoundException("Rol no encontrado"));

        if (in.nombreRol()!=null &&
                !in.nombreRol().equalsIgnoreCase(r.getNombreRol()) &&
                rolRepo.existsByNombreRolIgnoreCase(in.nombreRol())) {
            throw new ConflictException("Ya existe un rol con ese nombre");
        }
        r.setNombreRol(in.nombreRol());
        return toView(rolRepo.save(r));
    }

    @Transactional(readOnly = true)
    public RolView findById(Long id) {
        return rolRepo.findById(id).map(this::toView)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado"));
    }

    @Transactional(readOnly = true)
    public PageResponse<RolView> list(Integer page, Integer size) {
        Page<Rol> p = rolRepo.findAll(PageRequestUtil.of(page, size, Sort.by("nombreRol").ascending()));
        return PageMapper.map(p, this::toView);
    }

    @Transactional
    public boolean delete(Long id) {
        if (!rolRepo.existsById(id)) return false;
        rolRepo.deleteById(id);
        return true;
    }

    private RolView toView(Rol r) {
        return new RolView(r.getIdRol(), r.getNombreRol());
    }
}