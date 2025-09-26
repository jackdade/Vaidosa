package com.br.Vaidosa.Views;

import com.br.Vaidosa.Entidades.Simulacao;
import com.br.Vaidosa.Entidades.Produto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SimulacaoViewModel {

    private Long id;
    private String nomePessoa;
    private String cpf;
    private BigDecimal valorSegurado;
    private String numeroContratoEmprestimo;
    private LocalDate fimContratoEmprestimo;
    private LocalDate dataNascimento;
    private Long produtoId;         // Apenas referência ao produto
    private String produtoNome;     // Nome do produto
    private LocalDate dataSimulacao;
    private BigDecimal valorTotalPremio;

    public SimulacaoViewModel() {
    }

    public SimulacaoViewModel(Simulacao simulacao) {
        this.id = simulacao.getId();
        this.nomePessoa = simulacao.getNomePessoa();
        this.cpf = simulacao.getCpf();
        this.valorSegurado = simulacao.getValorSegurado();
        this.numeroContratoEmprestimo = simulacao.getNumeroContratoEmprestimo();
        this.fimContratoEmprestimo = simulacao.getFimContratoEmprestimo();
        this.dataNascimento = simulacao.getDataNascimento();
        this.dataSimulacao = simulacao.getDataSimulacao();
        this.valorTotalPremio = simulacao.getValorTotalPremio();

        Produto produto = simulacao.getProdutoEscolhido();
        if (produto != null) {
            this.produtoId = produto.getId();
            this.produtoNome = produto.getNome();
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public BigDecimal getValorSegurado() {
        return valorSegurado;
    }

    public void setValorSegurado(BigDecimal valorSegurado) {
        this.valorSegurado = valorSegurado;
    }

    public String getNumeroContratoEmprestimo() {
        return numeroContratoEmprestimo;
    }

    public void setNumeroContratoEmprestimo(String numeroContratoEmprestimo) {
        this.numeroContratoEmprestimo = numeroContratoEmprestimo;
    }

    public LocalDate getFimContratoEmprestimo() {
        return fimContratoEmprestimo;
    }

    public void setFimContratoEmprestimo(LocalDate fimContratoEmprestimo) {
        this.fimContratoEmprestimo = fimContratoEmprestimo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public LocalDate getDataSimulacao() {
        return dataSimulacao;
    }

    public void setDataSimulacao(LocalDate dataSimulacao) {
        this.dataSimulacao = dataSimulacao;
    }

    public BigDecimal getValorTotalPremio() {
        return valorTotalPremio;
    }

    public void setValorTotalPremio(BigDecimal valorTotalPremio) {
        this.valorTotalPremio = valorTotalPremio;
    }
}
