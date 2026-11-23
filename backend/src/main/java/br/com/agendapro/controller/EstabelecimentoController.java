package br.com.agendapro.controller;

import br.com.agendapro.dto.EstabelecimentoRequest;
import br.com.agendapro.dto.EstabelecimentoResponse;
import br.com.agendapro.service.EstabelecimentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/estabelecimentos")
public class EstabelecimentoController {
    private final EstabelecimentoService service;
    public EstabelecimentoController(EstabelecimentoService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(defaultValue = "") String nome,
        @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.listar(nome, pageable).map(EstabelecimentoResponse::from));
    }
    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(EstabelecimentoResponse.from(service.buscar(id)));
    }
    @PostMapping
    public ResponseEntity<EstabelecimentoResponse> criar(@Valid @RequestBody EstabelecimentoRequest request) {
        var salvo = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(EstabelecimentoResponse.from(salvo));
    }
    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody EstabelecimentoRequest request) {
        return ResponseEntity.ok(EstabelecimentoResponse.from(service.atualizar(id, request)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) { service.desativar(id); return ResponseEntity.noContent().build(); }
    @GetMapping("/proprietario")
    public ResponseEntity<?> listarDoProprietario(@RequestParam String email, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.listarDoProprietario(email, pageable).map(EstabelecimentoResponse::from));
    }
}
