
package br.com.agendapro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstabelecimentoRequest(

    @NotBlank
    @Size(max = 120)
    String nome,

    @Size(max = 500)
    String descricao,

    @Size(max = 30)
    String telefone,

    @Size(max = 220)
    String endereco

) {}
