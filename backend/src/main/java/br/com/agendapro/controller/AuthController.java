package br.com.agendapro.controller;

import br.com.agendapro.dto.LoginRequest;
import br.com.agendapro.dto.LoginResponse;
import br.com.agendapro.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse resposta = authService.login(request);

        return ResponseEntity.ok(resposta);
    }
}