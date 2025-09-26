package com.ep18.couriersync.backend.customers.service;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import com.ep18.couriersync.backend.common.pagination.PageMapper;
import com.ep18.couriersync.backend.common.pagination.PageRequestUtil;
import com.ep18.couriersync.backend.common.exception.ConflictException;
import com.ep18.couriersync.backend.common.exception.NotFoundException;
import com.ep18.couriersync.backend.common.exception.ValidationException;
import com.ep18.couriersync.backend.customers.domain.Ciudad;
import com.ep18.couriersync.backend.customers.domain.Departamento;
import com.ep18.couriersync.backend.customers.domain.Rol;
import com.ep18.couriersync.backend.customers.domain.Usuario;
import com.ep18.couriersync.backend.customers.dto.UsuarioDTOs.CreateUsuarioInput;
import com.ep18.couriersync.backend.customers.dto.UsuarioDTOs.UpdateUsuarioInput;
import com.ep18.couriersync.backend.customers.dto.UsuarioDTOs.UsuarioView;
import com.ep18.couriersync.backend.customers.repository.CiudadRepository;
import com.ep18.couriersync.backend.customers.repository.DepartamentoRepository;
import com.ep18.couriersync.backend.customers.repository.RolRepository;
import com.ep18.couriersync.backend.customers.repository.UsuarioRepository;
import com.ep18.couriersync.backend.customers.validator.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final CiudadRepository ciudadRepo;
    private final DepartamentoRepository departamentoRepo;
    private final RolRepository rolRepo;

    @Transactional
    public UsuarioView create(CreateUsuarioInput in) {
        // Unicidad
        if (usuarioRepo.existsByCorreoIgnoreCase(in.correo()))
            throw new ConflictException("El correo ya está registrado");
        if (usuarioRepo.existsByTelefono(in.telefono()))
            throw new ConflictException("El teléfono ya está registrado");

        // FKs
        Ciudad ciudad = ciudadRepo.findById(in.idCiudad())
                .orElseThrow(() -> new NotFoundException("Ciudad no encontrada"));
        Departamento depto = departamentoRepo.findById(in.idDepartamento())
                .orElseThrow(() -> new NotFoundException("Departamento no encontrado"));
        Rol rol = rolRepo.findById(in.idRol())
                .orElseThrow(() -> new NotFoundException("Rol no encontrado"));

        // Coherencia: la ciudad pertenece al departamento indicado
        UsuarioValidator.assertCiudadPerteneceADepartamento(ciudad, depto.getIdDepartamento());

        Usuario u = new Usuario();
        u.setNombre(in.nombre());
        u.setCorreo(in.correo());
        u.setTelefono(in.telefono());
        u.setFechaRegistro(in.fechaRegistro());
        u.setDetalleDireccion(in.detalleDireccion());
        u.setCiudad(ciudad);
        u.setDepartamento(depto);
        u.setRol(rol);

        return toView(usuarioRepo.save(u));
    }

    @Transactional
    public UsuarioView update(UpdateUsuarioInput in) {
        Usuario u = usuarioRepo.findById(in.idUsuario())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (in.correo()!=null && !in.correo().equalsIgnoreCase(u.getCorreo())
                && usuarioRepo.existsByCorreoIgnoreCase(in.correo())) {
            throw new ConflictException("El correo ya está registrado");
        }
        if (in.telefono()!=null && !in.telefono().equals(u.getTelefono())
                && usuarioRepo.existsByTelefono(in.telefono())) {
            throw new ConflictException("El teléfono ya está registrado");
        }

        // Aplicar cambios simples
        if (in.nombre()!=null) u.setNombre(in.nombre());
        if (in.correo()!=null) u.setCorreo(in.correo());
        if (in.telefono()!=null) u.setTelefono(in.telefono());
        if (in.fechaRegistro()!=null) u.setFechaRegistro(in.fechaRegistro());
        if (in.detalleDireccion()!=null) u.setDetalleDireccion(in.detalleDireccion());

        // Cambios de FKs (revalidando coherencia)
        Ciudad ciudad = (in.idCiudad()!=null)
                ? ciudadRepo.findById(in.idCiudad())
                .orElseThrow(() -> new NotFoundException("Ciudad no encontrada"))
                : u.getCiudad();

        Departamento depto = (in.idDepartamento()!=null)
                ? departamentoRepo.findById(in.idDepartamento())
                .orElseThrow(() -> new NotFoundException("Departamento no encontrado"))
                : u.getDepartamento();

        if (in.idCiudad()!=null || in.idDepartamento()!=null) {
            UsuarioValidator.assertCiudadPerteneceADepartamento(ciudad, depto.getIdDepartamento());
            u.setCiudad(ciudad);
            u.setDepartamento(depto);
        }

        if (in.idRol()!=null) {
            Rol rol = rolRepo.findById(in.idRol())
                    .orElseThrow(() -> new NotFoundException("Rol no encontrado"));
            u.setRol(rol);
        }

        return toView(usuarioRepo.save(u));
    }

    @Transactional(readOnly = true)
    public UsuarioView findById(Long id) {
        return usuarioRepo.findById(id).map(this::toView)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    @Transactional(readOnly = true)
    public PageResponse<UsuarioView> search(String q, Integer page, Integer size) {
        Page<Usuario> p = usuarioRepo.findByNombreContainingIgnoreCase(
                q == null ? "" : q, PageRequestUtil.of(page, size, Sort.by("createdAt").descending()));
        return PageMapper.map(p, this::toView);
    }

    @Transactional(readOnly = true)
    public PageResponse<UsuarioView> listByCiudad(Long idCiudad, Integer page, Integer size) {
        Page<Usuario> p = usuarioRepo.findAllByCiudad_IdCiudad(
                idCiudad, PageRequestUtil.of(page, size, Sort.by("createdAt").descending()));
        return PageMapper.map(p, this::toView);
    }

    @Transactional(readOnly = true)
    public PageResponse<UsuarioView> listByDepartamento(Long idDepto, Integer page, Integer size) {
        Page<Usuario> p = usuarioRepo.findAllByDepartamento_IdDepartamento(
                idDepto, PageRequestUtil.of(page, size, Sort.by("createdAt").descending()));
        return PageMapper.map(p, this::toView);
    }

    @Transactional(readOnly = true)
    public PageResponse<UsuarioView> listByRol(Long idRol, Integer page, Integer size) {
        Page<Usuario> p = usuarioRepo.findAllByRol_IdRol(
                idRol, PageRequestUtil.of(page, size, Sort.by("createdAt").descending()));
        return PageMapper.map(p, this::toView);
    }

    @Transactional
    public boolean delete(Long id) {
        if (!usuarioRepo.existsById(id)) return false;
        usuarioRepo.deleteById(id);
        return true;
    }

    private UsuarioView toView(Usuario u) {
        return new UsuarioView(
                u.getIdUsuario(),
                u.getNombre(),
                u.getCorreo(),
                u.getTelefono(),
                u.getFechaRegistro(),
                u.getDetalleDireccion(),
                u.getCiudad().getIdCiudad(),
                u.getCiudad().getNombreCiudad(),
                u.getDepartamento().getIdDepartamento(),
                u.getDepartamento().getNombreDepartamento(),
                u.getRol().getIdRol(),
                u.getRol().getNombreRol()
        );
    }
}