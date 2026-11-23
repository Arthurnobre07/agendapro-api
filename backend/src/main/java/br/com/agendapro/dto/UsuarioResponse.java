
package br.com.agendapro.dto;

import br.com.agendapro.entity.PapelUsuario;
import br.com.agendapro.entity.Usuario;
import java.time.LocalDateTime;
import java.util.Set;

public record UsuarioResponse(
    Long id,
    String nome,
    String email,
    String telefone,
    Boolean ativo,
    Set<PapelUsuario> papeis,
    LocalDateTime criadoEm
) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getTelefone(),
            usuario.getAtivo(),
            Set.copyOf(usuario.getPapeis()),
            usuario.getCriadoEm()
        );
    }
}