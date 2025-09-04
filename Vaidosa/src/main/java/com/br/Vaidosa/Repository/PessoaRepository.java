package com.br.Vaidosa.Repository;

import com.br.Vaidosa.Entidades.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Pessoa findByCpf(String cpf);

    Pessoa findByCodigo(Long codigo);
    Pessoa findByCodigoAndCpf(Long codigo, String cpf);

}
