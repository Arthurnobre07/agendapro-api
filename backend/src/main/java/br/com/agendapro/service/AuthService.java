package br.com.agendapro.service;

import br.com.agendapro.config.JwtService;
import br.com.agendapro.dto.LoginRequest;
import br.com.agendapro.dto.LoginResponse;
import br.com.agendapro.entity.Usuario;
import br.com.agendapro.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository
                .findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() ->
                        new IllegalArgumentException("E-mail ou senha inválidos."));

        if (!usuario.getAtivo()) {
            throw new IllegalArgumentException("Usuário inativo.");
        }

        if (!passwordEncoder.matches(
                request.senha(),
                usuario.getSenha())) {

            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }

        String token = jwtService.gerarToken(usuario.getEmail());

        return new LoginResponse(
                token,
                "Bearer",
                1800
        );
    }
}