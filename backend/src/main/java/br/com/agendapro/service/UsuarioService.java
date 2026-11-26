
package br.com.agendapro.service;

import br.com.agendapro.entity.PapelUsuario;
import br.com.agendapro.entity.Usuario;
import br.com.agendapro.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario criar(String nome, String email, String senha, String telefone) {
        return criarComPapel(
                nome, email, senha, telefone, PapelUsuario.CLIENTE
        );
    }

    @Transactional
    public Usuario criarProprietario(
            String nome,
            String email,
            String senha,
            String telefone) {

        return criarComPapel(
                nome, email, senha, telefone, PapelUsuario.PROPRIETARIO
        );
    }

    private Usuario criarComPapel(
            String nome,
            String email,
            String senha,
            String telefone,
            PapelUsuario papel) {

        email = email.trim().toLowerCase();

        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado."
            );
        }

        String senhaCriptografada = passwordEncoder.encode(senha);

        Usuario usuario = new Usuario(
                nome,
                email,
                senhaCriptografada,
                telefone
        );

        usuario.getPapeis().add(papel);

        return repository.save(usuario);
    }
}
