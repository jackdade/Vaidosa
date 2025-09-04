package com.br.Vaidosa.Services;

import com.br.Vaidosa.Entidades.Pessoa;
import com.br.Vaidosa.Repository.PessoaRepository;
import com.br.Vaidosa.Views.PessoaViewModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Set;


@Service
public class PessoaService {

    @Autowired
    private Validator validator;

    @Autowired
    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public Page<Pessoa> buscaTodasPessoasPorPagina(Pageable pageable) {
        return pessoaRepository.findAll(pageable);
    }

    public Pessoa buscarPessoa(Long codigo, String cpf) {
        if (codigo != null && cpf != null && !cpf.isEmpty()) {
            return pessoaRepository.findByCodigoAndCpf(codigo, cpf);
        } else if (codigo != null) {
            return pessoaRepository.findByCodigo(codigo);
        } else if (cpf != null && !cpf.isEmpty()) {
            return pessoaRepository.findByCpf(cpf);
        } else {
            return null;
        }
    }


    public Pessoa atualizar(PessoaViewModel pessoaVM) {


        Pessoa pessoa = buscarPessoa(pessoaVM.getCodigo(), null);


        if (pessoaVM.getNome() != null && !pessoaVM.getNome().isEmpty()) {
            pessoa.setNome(pessoaVM.getNome());
        }
        if (pessoaVM.getCpf() != null && !pessoaVM.getCpf().isEmpty()) {
            pessoa.setCpf(pessoaVM.getCpf());
        }
        if (pessoaVM.getEmail() != null && !pessoaVM.getEmail().isEmpty()) {
            pessoa.setEmail(pessoaVM.getEmail());
        }
        if (pessoaVM.getTelefone() != null && !pessoaVM.getTelefone().isEmpty()) {
            pessoa.setTelefone(pessoaVM.getTelefone());
        }
        if (pessoaVM.getDataNascimento() != null) {
            pessoa.setDataNascimento(pessoaVM.getDataNascimento());
        }
        if (pessoaVM.getEndereco() != null && !pessoaVM.getEndereco().isEmpty()) {
            pessoa.setEndereco(pessoaVM.getEndereco());
        }
        if (pessoaVM.getCep() != null && !pessoaVM.getCep().isEmpty()) {
            pessoa.setCep(pessoaVM.getCep());
        }
        if (pessoaVM.getCidade() != null && !pessoaVM.getCidade().isEmpty()) {
            pessoa.setCidade(pessoaVM.getCidade());
        }
        if (pessoaVM.getEstado() != null && !pessoaVM.getEstado().isEmpty()) {
            pessoa.setEstado(pessoaVM.getEstado());
        }
        if (pessoaVM.getAtivo() != null) {
            pessoa.setAtivo(pessoaVM.getAtivo());
        }

        pessoa.setDataAtualizacao(LocalDateTime.now());

        return pessoaRepository.save(pessoa);
    }

    public void deletar(Long codigo) {
        pessoaRepository.deleteById(codigo);
    }

    public Pessoa inserir(PessoaViewModel pessoaVM) {

        Pessoa pessoa = new Pessoa();

        pessoa.setNome(pessoaVM.getNome());
        pessoa.setCpf(pessoaVM.getCpf());
        pessoa.setEmail(pessoaVM.getEmail());
        pessoa.setTelefone(pessoaVM.getTelefone());
        pessoa.setDataNascimento(pessoaVM.getDataNascimento());
        pessoa.setEndereco(pessoaVM.getEndereco());
        pessoa.setCep(pessoaVM.getCep());
        pessoa.setCidade(pessoaVM.getCidade());
        pessoa.setEstado(pessoaVM.getEstado());
        pessoa.setAtivo(pessoaVM.getAtivo() != null ? pessoaVM.getAtivo() : true);
        pessoa.setDataAtualizacao(LocalDateTime.now());
        pessoa.setDataCadastro(LocalDateTime.now());


        Set<ConstraintViolation<Pessoa>> violations = validator.validate(pessoa);
        if (!violations.isEmpty()) {
            String erros = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                    .orElse("Erro de validação");
            throw new ValidationException(erros);
        }


        return pessoaRepository.save(pessoa);
    }
}
