package com.example.demo.service.seguridad;

import com.example.demo.domain.seguridad.Rol;
import com.example.demo.domain.seguridad.Usuario;
import com.example.demo.dto.seguridad.*;
import com.example.demo.mapper.seguridad.SeguridadMapper;
import com.example.demo.repository.seguridad.RolRepository;
import com.example.demo.repository.seguridad.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final SeguridadMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UsuarioDto> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public UsuarioDto findById(Long id) {
        return usuarioRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + id));
    }

    @Transactional
    public UsuarioDto create(UsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("El username ya está en uso");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("El email ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setActivo(true);
        
        usuario.setRoles(getRolesByIds(request.getRolIds()));
        
        return mapper.toDto(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDto update(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + id));

        usuarioRepository.findByUsername(request.getUsername())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new IllegalStateException("El username ya está en uso por otro usuario");
                });

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        
        // La actualización no cambia el password si viene vacío, pero el request lo hace obligatorio
        // En una app real suele separarse el endpoint de update info del de reset/change password
        usuario.setRoles(getRolesByIds(request.getRolIds()));

        return mapper.toDto(usuarioRepository.save(usuario));
    }

    @Transactional
    public void softDelete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + id));
        usuario.setActivo(false);
        usuario.setRefreshTokenHash(null); // Invalidar sesión si existía
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void cambiarPassword(String username, CambioPasswordRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPasswordHash())) {
            throw new IllegalStateException("La contraseña actual es incorrecta");
        }

        usuario.setPasswordHash(passwordEncoder.encode(request.getPasswordNueva()));
        // Recomendado: invalidar refresh token para forzar re-login
        usuario.setRefreshTokenHash(null);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public UsuarioDto asignarRoles(Long id, AsignarRolesRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
                
        usuario.setRoles(getRolesByIds(request.getRolIds()));
        return mapper.toDto(usuarioRepository.save(usuario));
    }

    private Set<Rol> getRolesByIds(List<Long> ids) {
        Set<Rol> roles = new HashSet<>();
        if (ids != null && !ids.isEmpty()) {
            List<Rol> found = rolRepository.findAllById(ids);
            if (found.size() != ids.size()) {
                throw new EntityNotFoundException("Uno o más roles no fueron encontrados");
            }
            roles.addAll(found);
        }
        return roles;
    }
}
