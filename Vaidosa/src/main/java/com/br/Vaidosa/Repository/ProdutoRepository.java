package com.br.Vaidosa.Repository;

import com.br.Vaidosa.Entidades.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Produto findFirstByNome(String nome);

}
