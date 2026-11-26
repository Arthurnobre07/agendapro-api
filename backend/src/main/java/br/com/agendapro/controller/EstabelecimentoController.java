
package br.com.agendapro.controller;

import br.com.agendapro.dto.EstabelecimentoRequest;
import br.com.agendapro.dto.EstabelecimentoResponse;
import br.com.agendapro.service.EstabelecimentoService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/estabelecimentos")
public class EstabelecimentoController {

    private final EstabelecimentoService service;

    public EstabelecimentoController(EstabelecimentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> listarDoProprietario(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "nome")
            Pageable pageable) {

        return ResponseEntity.ok(
                service.listarDoProprietario(
                        authentication.getName(),
                        pageable
                ).map(EstabelecimentoResponse::from)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponse> buscar(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                EstabelecimentoResponse.from(
                        service.buscar(id, authentication.getName())
                )
        );
    }

    @PostMapping
    public ResponseEntity<EstabelecimentoResponse> criar(
            @Valid @RequestBody EstabelecimentoRequest request,
            Authentication authentication) {

        var salvo = service.criar(
                request,
                authentication.getName()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(EstabelecimentoResponse.from(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EstabelecimentoRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                EstabelecimentoResponse.from(
                        service.atualizar(
                                id,
                                request,
                                authentication.getName()
                        )
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(
            @PathVariable Long id,
            Authentication authentication) {

        service.desativar(id, authentication.getName());

        return ResponseEntity.noContent().build();
    }
}
