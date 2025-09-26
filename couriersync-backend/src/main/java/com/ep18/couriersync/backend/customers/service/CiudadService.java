package com.ep18.couriersync.backend.customers.service;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.common.pagination.PageMapper;
import com.ep18.couriersync.backend.common.pagination.PageRequestUtil;
import com.ep18.couriersync.backend.common.exception.ConflictException;
import com.ep18.couriersync.backend.common.exception.NotFoundException;
import com.ep18.couriersync.backend.customers.domain.Ciudad;
import com.ep18.couriersync.backend.customers.domain.Departamento;
import com.ep18.couriersync.backend.customers.dto.CiudadDTOs.CreateCiudadInput;
import com.ep18.couriersync.backend.customers.dto.CiudadDTOs.CiudadView;
import com.ep18.couriersync.backend.customers.dto.CiudadDTOs.UpdateCiudadInput;
import com.ep18.couriersync.backend.customers.repository.CiudadRepository;
import com.ep18.couriersync.backend.customers.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CiudadService {

    private final CiudadRepository ciudadRepo;
    private final DepartamentoRepository departamentoRepo;

    @Transactional
    public CiudadView create(CreateCiudadInput in) {
        Departamento depto = departamentoRepo.findById(in.idDepartamento())
                .orElseThrow(() -> new NotFoundException("Departamento no encontrado"));

        if (ciudadRepo.existsByNombreCiudadIgnoreCaseAndDepartamento_IdDepartamento(
                in.nombreCiudad(), in.idDepartamento())) {
            throw new ConflictException("Ya existe una ciudad con ese nombre en el departamento");
        }

        Ciudad c = new Ciudad();
        c.setNombreCiudad(in.nombreCiudad());
        c.setDepartamento(depto);

        return toView(ciudadRepo.save(c));
    }

    @Transactional
    public CiudadView update(UpdateCiudadInput in) {
        Ciudad c = ciudadRepo.findById(in.idCiudad())
                .orElseThrow(() -> new NotFoundException("Ciudad no encontrada"));

        if (in.nombreCiudad()!=null &&
                !in.nombreCiudad().equalsIgnoreCase(c.getNombreCiudad())) {
            boolean exists = ciudadRepo.existsByNombreCiudadIgnoreCaseAndDepartamento_IdDepartamento(
                    in.nombreCiudad(), c.getDepartamento().getIdDepartamento());
            if (exists) throw new ConflictException("Ya existe ciudad con ese nombre en el departamento");
            c.setNombreCiudad(in.nombreCiudad());
        }

        if (in.idDepartamento()!=null &&
                !in.idDepartamento().equals(c.getDepartamento().getIdDepartamento())) {
            Departamento nuevo = departamentoRepo.findById(in.idDepartamento())
                    .orElseThrow(() -> new NotFoundException("Departamento no encontrado"));

            // si cambia de departamento, revalidar unicidad en el nuevo depto
            boolean exists = ciudadRepo.existsByNombreCiudadIgnoreCaseAndDepartamento_IdDepartamento(
                    c.getNombreCiudad(), nuevo.getIdDepartamento());
            if (exists) throw new ConflictException("Ya existe ciudad con ese nombre en el nuevo departamento");

            c.setDepartamento(nuevo);
        }

        return toView(ciudadRepo.save(c));
    }

    @Transactional(readOnly = true)
    public CiudadView findById(Long id) {
        return ciudadRepo.findById(id).map(this::toView)
                .orElseThrow(() -> new NotFoundException("Ciudad no encontrada"));
    }

    @Transactional(readOnly = true)
    public PageResponse<CiudadView> listByDepartamento(Long idDepartamento, Integer page, Integer size) {
        Page<Ciudad> p = ciudadRepo.findAllByDepartamento_IdDepartamento(
                idDepartamento, PageRequestUtil.of(page, size, Sort.by("nombreCiudad").ascending()));
        return PageMapper.map(p, this::toView);
    }

    @Transactional(readOnly = true)
    public PageResponse<CiudadView> search(String q, Integer page, Integer size) {
        Page<Ciudad> p = ciudadRepo.findByNombreCiudadContainingIgnoreCase(
                q == null ? "" : q, PageRequestUtil.of(page, size, Sort.by("nombreCiudad").ascending()));
        return PageMapper.map(p, this::toView);
    }

    @Transactional
    public boolean delete(Long id) {
        if (!ciudadRepo.existsById(id)) return false;
        ciudadRepo.deleteById(id);
        return true;
    }

    private CiudadView toView(Ciudad c) {
        return new CiudadView(
                c.getIdCiudad(),
                c.getNombreCiudad(),
                c.getDepartamento().getIdDepartamento(),
                c.getDepartamento().getNombreDepartamento()
        );
    }
}