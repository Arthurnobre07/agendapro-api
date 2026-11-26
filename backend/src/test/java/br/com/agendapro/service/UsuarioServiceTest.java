
package br.com.agendapro.service;

import br.com.agendapro.entity.PapelUsuario;
import br.com.agendapro.entity.Usuario;
import br.com.agendapro.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    private String senhaOriginal;
    private String senhaCriptografada;

    @BeforeEach
    void configurar() {
        senhaOriginal = "TesteAgenda123";
        senhaCriptografada = "$2a$10$senhaCriptografada";
    }

    @Test
    void deveCadastrarClienteComPapelCliente() {
        when(repository.existsByEmail("cliente@teste.com"))
                .thenReturn(false);

        when(passwordEncoder.encode(senhaOriginal))
                .thenReturn(senhaCriptografada);

        when(repository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuario = service.criar(
                "Cliente Teste",
                "cliente@teste.com",
                senhaOriginal,
                "82999999999"
        );

        assertTrue(usuario.getPapeis().contains(PapelUsuario.CLIENTE));
        assertEquals(senhaCriptografada, usuario.getSenha());

        verify(repository).save(usuario);
    }

    @Test
    void deveCadastrarProprietarioComPapelProprietario() {
        when(repository.existsByEmail("proprietario@teste.com"))
                .thenReturn(false);

        when(passwordEncoder.encode(senhaOriginal))
                .thenReturn(senhaCriptografada);

        when(repository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuario = service.criarProprietario(
                "Proprietario Teste",
                "proprietario@teste.com",
                senhaOriginal,
                "82999999999"
        );

        assertTrue(usuario.getPapeis().contains(PapelUsuario.PROPRIETARIO));
        assertEquals(senhaCriptografada, usuario.getSenha());

        verify(repository).save(usuario);
    }

    @Test
    void naoDeveCadastrarEmailDuplicado() {
        when(repository.existsByEmail("cliente@teste.com"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.criar(
                        "Cliente Teste",
                        "cliente@teste.com",
                        senhaOriginal,
                        "82999999999"
                )
        );

        verify(repository, never()).save(any(Usuario.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void deveCriptografarSenhaAntesDeSalvar() {
        when(repository.existsByEmail("cliente@teste.com"))
                .thenReturn(false);

        when(passwordEncoder.encode(senhaOriginal))
                .thenReturn(senhaCriptografada);

        when(repository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuario = service.criar(
                "Cliente Teste",
                "cliente@teste.com",
                senhaOriginal,
                "82999999999"
        );

        assertNotEquals(senhaOriginal, usuario.getSenha());
        assertEquals(senhaCriptografada, usuario.getSenha());

        verify(passwordEncoder).encode(senhaOriginal);
    }

    @Test
    void deveSalvarUsuarioNoRepositorio() {
        when(repository.existsByEmail("cliente@teste.com"))
                .thenReturn(false);

        when(passwordEncoder.encode(senhaOriginal))
                .thenReturn(senhaCriptografada);

        when(repository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.criar(
                "Cliente Teste",
                "cliente@teste.com",
                senhaOriginal,
                "82999999999"
        );

        verify(repository, times(1)).save(any(Usuario.class));
    }
}
