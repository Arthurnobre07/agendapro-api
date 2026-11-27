
package br.com.agendapro.service;

import br.com.agendapro.dto.EstabelecimentoRequest;
import br.com.agendapro.entity.Estabelecimento;
import br.com.agendapro.entity.PapelUsuario;
import br.com.agendapro.entity.Usuario;
import br.com.agendapro.exception.RecursoNaoEncontradoException;
import br.com.agendapro.repository.EstabelecimentoRepository;
import br.com.agendapro.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstabelecimentoServiceTest {

    @Mock
    private EstabelecimentoRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EstabelecimentoService service;

    private Usuario proprietario;
    private Estabelecimento estabelecimento;

    private final String email = "proprietario@teste.com";

    @BeforeEach
    void configurar() {

        proprietario = new Usuario(
                "Proprietario Teste",
                email,
                "senhaCriptografada",
                "82999999999"
        );

        // Define um ID para simular uma entidade persistida.
        ReflectionTestUtils.setField(proprietario, "id", 1L);

        proprietario.getPapeis().add(PapelUsuario.PROPRIETARIO);

        estabelecimento = new Estabelecimento(
                "Farmacia Teste",
                "Descricao de teste",
                "82999999999",
                "Maceio - AL",
                proprietario
        );
    }

    @Test
    void deveCriarEstabelecimentoVinculadoAoProprietario() {

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(proprietario));

        when(repository.save(any(Estabelecimento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EstabelecimentoRequest request = new EstabelecimentoRequest(
                "Farmacia Teste",
                "Descricao de teste",
                "82999999999",
                "Maceio - AL"
        );

        Estabelecimento resultado = service.criar(request, email);

        assertEquals("Farmacia Teste", resultado.getNome());
        assertEquals(proprietario, resultado.getProprietario());
        assertEquals(email, resultado.getProprietarioEmail());
        assertTrue(resultado.getAtivo());

        verify(repository).save(any(Estabelecimento.class));
    }

    @Test
    void deveListarApenasEstabelecimentosDoProprietario() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Estabelecimento> pagina = new PageImpl<>(
                List.of(estabelecimento),
                pageable,
                1
        );

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(proprietario));

        when(repository.findByProprietarioIdAndAtivoTrue(
                1L, pageable
        )).thenReturn(pagina);

        Page<Estabelecimento> resultado =
                service.listarDoProprietario(email, pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(
                "Farmacia Teste",
                resultado.getContent().get(0).getNome()
        );

        verify(repository).findByProprietarioIdAndAtivoTrue(
                1L, pageable
        );
    }

    @Test
    void deveBuscarEstabelecimentoDoProprietario() {

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(proprietario));

        when(repository.findByIdAndProprietarioId(4L, 1L))
                .thenReturn(Optional.of(estabelecimento));

        Estabelecimento resultado = service.buscar(4L, email);

        assertEquals("Farmacia Teste", resultado.getNome());
        assertEquals(proprietario, resultado.getProprietario());
    }

    @Test
    void naoDeveBuscarEstabelecimentoDeOutroProprietario() {

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(proprietario));

        when(repository.findByIdAndProprietarioId(4L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.buscar(4L, email)
        );
    }

    @Test
    void deveAtualizarEstabelecimentoDoProprietario() {

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(proprietario));

        when(repository.findByIdAndProprietarioId(4L, 1L))
                .thenReturn(Optional.of(estabelecimento));

        when(repository.save(any(Estabelecimento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EstabelecimentoRequest request = new EstabelecimentoRequest(
                "Farmacia Atualizada",
                "Nova descricao",
                "82988888888",
                "Novo endereco"
        );

        Estabelecimento resultado =
                service.atualizar(4L, request, email);

        assertEquals("Farmacia Atualizada", resultado.getNome());
        assertEquals("Nova descricao", resultado.getDescricao());

        verify(repository).save(estabelecimento);
    }

    @Test
    void naoDeveAtualizarEstabelecimentoDeOutroProprietario() {

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(proprietario));

        when(repository.findByIdAndProprietarioId(4L, 1L))
                .thenReturn(Optional.empty());

        EstabelecimentoRequest request = new EstabelecimentoRequest(
                "Tentativa de invasao",
                "Descricao",
                "82988888888",
                "Endereco"
        );

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.atualizar(4L, request, email)
        );

        verify(repository, never()).save(any(Estabelecimento.class));
    }

    @Test
    void deveDesativarEstabelecimentoDoProprietario() {

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(proprietario));

        when(repository.findByIdAndProprietarioId(4L, 1L))
                .thenReturn(Optional.of(estabelecimento));

        assertTrue(estabelecimento.getAtivo());

        service.desativar(4L, email);

        assertFalse(estabelecimento.getAtivo());
    }

    @Test
    void naoDeveDesativarEstabelecimentoDeOutroProprietario() {

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(proprietario));

        when(repository.findByIdAndProprietarioId(4L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.desativar(4L, email)
        );

        assertTrue(estabelecimento.getAtivo());
    }
}