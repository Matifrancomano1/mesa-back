package com.example.demo.service.auth;

import com.example.demo.domain.seguridad.Usuario;
import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.dto.auth.RefreshRequest;
import com.example.demo.dto.seguridad.UsuarioDto;
import com.example.demo.mapper.seguridad.SeguridadMapper;
import com.example.demo.repository.seguridad.UsuarioRepository;
import com.example.demo.security.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final SeguridadMapper seguridadMapper;

    @Value("${app.jwt.access-token-expiry}")
    private long accessTokenExpiry;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();

        List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String accessToken = jwtUtil.generateAccessToken(usuario.getUsername(), authorities);
        String refreshToken = jwtUtil.generateRefreshToken(usuario.getUsername());

        usuario.setRefreshTokenHash(passwordEncoder.encode(refreshToken));
        usuarioRepository.save(usuario);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessTokenExpiry / 1000)
                .usuario(seguridadMapper.toResumenDto(usuario))
                .build();
    }

    @Transactional
    public LoginResponse refresh(RefreshRequest request) {
        String token = request.getRefreshToken();
        
        if (!jwtUtil.isTokenValid(token)) {
            throw new org.springframework.security.access.AccessDeniedException("Refresh token inválido");
        }

        Claims claims = jwtUtil.validateAndParseClaims(token);
        if (!"refresh".equals(claims.get("type"))) {
            throw new org.springframework.security.access.AccessDeniedException("El token no es de tipo refresh");
        }

        String username = claims.getSubject();
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("Usuario no encontrado"));

        if (usuario.getRefreshTokenHash() == null || !passwordEncoder.matches(token, usuario.getRefreshTokenHash())) {
            throw new org.springframework.security.access.AccessDeniedException("Refresh token revocado o no coincide");
        }

        List<String> authorities = usuario.getRoles().stream()
                .flatMap(r -> r.getFunciones().stream())
                .map(f -> f.getCodigo())
                .collect(Collectors.toList());

        String newAccessToken = jwtUtil.generateAccessToken(usuario.getUsername(), authorities);
        String newRefreshToken = jwtUtil.generateRefreshToken(usuario.getUsername());

        usuario.setRefreshTokenHash(passwordEncoder.encode(newRefreshToken));
        usuarioRepository.save(usuario);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(accessTokenExpiry / 1000)
                .usuario(seguridadMapper.toResumenDto(usuario))
                .build();
    }

    @Transactional
    public void logout(String username) {
        usuarioRepository.findByUsername(username).ifPresent(u -> {
            u.setRefreshTokenHash(null);
            usuarioRepository.save(u);
        });
    }
    
    @Transactional(readOnly = true)
    public UsuarioDto getMe(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow();
        return seguridadMapper.toDto(usuario);
    }
}
