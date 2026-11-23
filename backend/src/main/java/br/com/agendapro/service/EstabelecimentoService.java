package br.com.agendapro.service;

import br.com.agendapro.dto.EstabelecimentoRequest;
import br.com.agendapro.entity.Estabelecimento;
import br.com.agendapro.exception.RecursoNaoEncontradoException;
import br.com.agendapro.repository.EstabelecimentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstabelecimentoService {
    private final EstabelecimentoRepository repository;
    public EstabelecimentoService(EstabelecimentoRepository repository) { this.repository = repository; }

    @Transactional
    public Estabelecimento criar(EstabelecimentoRequest r) {
        return repository.save(new Estabelecimento(r.nome(), r.descricao(), r.telefone(), r.endereco(), r.proprietarioEmail()));
    }
    @Transactional(readOnly = true)
    public Page<Estabelecimento> listar(String nome, Pageable pageable) {
        return repository.findByAtivoTrueAndNomeContainingIgnoreCase(nome == null ? "" : nome, pageable);
    }
    @Transactional(readOnly = true)
    public Estabelecimento buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Estabelecimento não encontrado: " + id));
    }
    @Transactional
    public Estabelecimento atualizar(Long id, EstabelecimentoRequest r) {
        Estabelecimento e = buscar(id);
        e.atualizar(r.nome(), r.descricao(), r.telefone(), r.endereco());
        return repository.save(e);
    }
    @Transactional
    public void desativar(Long id) { buscar(id).desativar(); }
    @Transactional(readOnly = true)
    public Page<Estabelecimento> listarDoProprietario(String email, Pageable pageable) {
        return repository.findByProprietarioEmailIgnoreCase(email, pageable);
    }
}
