
package br.com.agendapro.service;

import br.com.agendapro.dto.EstabelecimentoRequest;
import br.com.agendapro.entity.Estabelecimento;
import br.com.agendapro.entity.PapelUsuario;
import br.com.agendapro.entity.Usuario;
import br.com.agendapro.exception.RecursoNaoEncontradoException;
import br.com.agendapro.repository.EstabelecimentoRepository;
import br.com.agendapro.repository.UsuarioRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstabelecimentoService {

    private final EstabelecimentoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public EstabelecimentoService(
            EstabelecimentoRepository repository,
            UsuarioRepository usuarioRepository) {

        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario buscarProprietario(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Usuário não encontrado."
                        )
                );

        if (!usuario.getAtivo()) {
            throw new AccessDeniedException("Usuário inativo.");
        }

        if (!usuario.getPapeis().contains(PapelUsuario.PROPRIETARIO)) {
            throw new AccessDeniedException(
                    "Apenas proprietários podem administrar estabelecimentos."
            );
        }

        return usuario;
    }

    private Estabelecimento buscarDoProprietario(
            Long id,
            Long proprietarioId) {

        return repository.findByIdAndProprietarioId(id, proprietarioId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Estabelecimento não encontrado."
                        )
                );
    }

    @Transactional
    public Estabelecimento criar(
            EstabelecimentoRequest request,
            String emailAutenticado) {

        Usuario proprietario = buscarProprietario(emailAutenticado);

        Estabelecimento estabelecimento = new Estabelecimento(
                request.nome(),
                request.descricao(),
                request.telefone(),
                request.endereco(),
                proprietario
        );

        return repository.save(estabelecimento);
    }

    @Transactional(readOnly = true)
    public Page<Estabelecimento> listar(
            String nome,
            Pageable pageable) {

        return repository.findByAtivoTrueAndNomeContainingIgnoreCase(
                nome == null ? "" : nome,
                pageable
        );
    }

    @Transactional(readOnly = true)
    public Estabelecimento buscar(
            Long id,
            String emailAutenticado) {

        Usuario proprietario = buscarProprietario(emailAutenticado);

        return buscarDoProprietario(id, proprietario.getId());
    }

    @Transactional
    public Estabelecimento atualizar(
            Long id,
            EstabelecimentoRequest request,
            String emailAutenticado) {

        Usuario proprietario = buscarProprietario(emailAutenticado);

        Estabelecimento estabelecimento = buscarDoProprietario(
                id,
                proprietario.getId()
        );

        estabelecimento.atualizar(
                request.nome(),
                request.descricao(),
                request.telefone(),
                request.endereco()
        );

        return repository.save(estabelecimento);
    }

    @Transactional
    public void desativar(
            Long id,
            String emailAutenticado) {

        Usuario proprietario = buscarProprietario(emailAutenticado);

        Estabelecimento estabelecimento = buscarDoProprietario(
                id,
                proprietario.getId()
        );

        estabelecimento.desativar();
    }

    @Transactional(readOnly = true)
    public Page<Estabelecimento> listarDoProprietario(
            String emailAutenticado,
            Pageable pageable) {

        Usuario proprietario = buscarProprietario(emailAutenticado);

        return repository.findByProprietarioIdAndAtivoTrue(
                proprietario.getId(),
                pageable
        );
    }
}
