
package br.com.agendapro.repository;

import br.com.agendapro.entity.Estabelecimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstabelecimentoRepository
        extends JpaRepository<Estabelecimento, Long> {

    Page<Estabelecimento> findByAtivoTrueAndNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );

    Page<Estabelecimento> findByProprietarioId(
            Long proprietarioId,
            Pageable pageable
    );

    Page<Estabelecimento> findByProprietarioIdAndAtivoTrue(
            Long proprietarioId,
            Pageable pageable
    );

    java.util.Optional<Estabelecimento> findByIdAndProprietarioId(
            Long id,
            Long proprietarioId
    );
}
