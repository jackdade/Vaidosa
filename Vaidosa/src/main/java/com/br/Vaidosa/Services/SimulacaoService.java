package com.br.Vaidosa.Services;

import com.br.Vaidosa.Entidades.Produto;
import com.br.Vaidosa.Entidades.Simulacao;
import com.br.Vaidosa.Repository.ProdutoRepository;
import com.br.Vaidosa.Repository.SimulacaoRepository;
import com.br.Vaidosa.Views.ProdutoViewModel;
import com.br.Vaidosa.Views.SimulacaoViewModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Service
public class SimulacaoService {

    @Autowired
    private Validator validator;

    @Autowired
    private SimulacaoRepository simulacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Simulacao gravarSimulacao(SimulacaoViewModel simulacaoViewModel) {

        Simulacao simulacao =  new Simulacao();
        simulacao.setNomePessoa(simulacaoViewModel.getNomePessoa());
        simulacao.setCpf(simulacaoViewModel.getCpf());
        simulacao.setValorSegurado(simulacaoViewModel.getValorSegurado());
        simulacao.setNumeroContratoEmprestimo(simulacaoViewModel.getNumeroContratoEmprestimo());
        simulacao.setFimContratoEmprestimo(simulacaoViewModel.getFimContratoEmprestimo());
        simulacao.setDataNascimento(simulacaoViewModel.getDataNascimento());
        simulacao.setDataSimulacao(LocalDate.now());
        simulacao.setValorTotalPremio(simulacaoViewModel.getValorTotalPremio());
        Produto produto = produtoRepository.findById(simulacaoViewModel.getProdutoId()).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        simulacao.setProdutoEscolhido(produto);
        Set<ConstraintViolation<Simulacao>> violations = validator.validate(simulacao);
        if (!violations.isEmpty()) {
            String erros = violations.stream().map(ConstraintViolation::getMessage).reduce((msg1, msg2) -> msg1 + "; " + msg2).orElse("Erro de validação");
            throw new ValidationException(erros);
        }


        return simulacaoRepository.save(simulacao);
    }

    public BigDecimal calcularPremio(SimulacaoViewModel simulacaoViewModel) {

        Produto produto = produtoRepository.findById(simulacaoViewModel.getProdutoId()).orElseThrow(() -> new RuntimeException("Produto não encontrado"));


        LocalDate dataAtual = LocalDate.now();
        LocalDate dataFim = simulacaoViewModel.getFimContratoEmprestimo();

        int meses = (dataFim.getYear() - dataAtual.getYear()) * 12
                + dataFim.getMonthValue() - dataAtual.getMonthValue() + 1;

        BigDecimal valorSegurado = simulacaoViewModel.getValorSegurado();
        BigDecimal taxaJuros = produto.getTaxaJuros();

        BigDecimal premio = valorSegurado
                .multiply(taxaJuros)
                .divide(BigDecimal.valueOf(1000),
                        2, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(meses));

        if (premio != null && produto.getValorMinimoPremio() != null) {
            if (premio.compareTo(produto.getValorMinimoPremio()) < 0) {
                premio = produto.getValorMinimoPremio();
            }
        }


        return  premio;
    }

    public List<SimulacaoViewModel> listarTodos() {
        List<Simulacao> simulacaos = simulacaoRepository.findAll();

        return simulacaos.stream()
                .map(SimulacaoViewModel::new)
                .toList();
    }

}
