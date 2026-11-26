
package br.com.agendapro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "estabelecimentos")
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String nome;

    @Size(max = 500)
    @Column(length = 500)
    private String descricao;

    @Size(max = 30)
    private String telefone;

    @Size(max = 220)
    private String endereco;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Usuario proprietario;

    protected Estabelecimento() {
    }

    public Estabelecimento(
            String nome,
            String descricao,
            String telefone,
            String endereco,
            Usuario proprietario) {

        this.nome = nome;
        this.descricao = descricao;
        this.telefone = telefone;
        this.endereco = endereco;
        this.proprietario = proprietario;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public Usuario getProprietario() {
        return proprietario;
    }

    public String getProprietarioEmail() {
        return proprietario.getEmail();
    }

    public void atualizar(
            String nome,
            String descricao,
            String telefone,
            String endereco) {

        this.nome = nome;
        this.descricao = descricao;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public void desativar() {
        this.ativo = false;
    }
}
