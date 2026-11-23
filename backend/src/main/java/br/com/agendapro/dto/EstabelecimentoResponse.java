package br.com.agendapro.dto;

import br.com.agendapro.entity.Estabelecimento;
import java.time.LocalDateTime;

public record EstabelecimentoResponse(Long id, String nome, String descricao, String telefone,
    String endereco, Boolean ativo, String proprietarioEmail, LocalDateTime criadoEm) {
    public static EstabelecimentoResponse from(Estabelecimento e) {
        return new EstabelecimentoResponse(e.getId(), e.getNome(), e.getDescricao(), e.getTelefone(),
            e.getEndereco(), e.getAtivo(), e.getProprietarioEmail(), e.getCriadoEm());
    }
}
