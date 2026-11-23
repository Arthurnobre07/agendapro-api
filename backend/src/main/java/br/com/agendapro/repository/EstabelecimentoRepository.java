package br.com.agendapro.repository;

import br.com.agendapro.entity.Estabelecimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Long> {
    Page<Estabelecimento> findByAtivoTrueAndNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<Estabelecimento> findByProprietarioEmailIgnoreCase(String email, Pageable pageable);
}
