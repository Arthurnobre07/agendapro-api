package br.com.agendapro.controller;

import br.com.agendapro.dto.UsuarioRequest;
import br.com.agendapro.dto.UsuarioResponse;
import br.com.agendapro.entity.Usuario;
import br.com.agendapro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(
            @Valid @RequestBody UsuarioRequest request) {

        Usuario usuario = service.criar(
                request.nome(),
                request.email(),
                request.senha(),
                request.telefone()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UsuarioResponse.de(usuario));
    }
}