package com.br.Vaidosa.Entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "simulacao")
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nomePessoa", nullable = false)
    private String nomePessoa;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "valorSegurado")
    private BigDecimal valorSegurado;

    @Column(name = "numeroContratoEmprestimo")
    private String numeroContratoEmprestimo;

    @Column(name = "fimContratoEmprestimo")
    private LocalDate fimContratoEmprestimo;

    @Column(name = "dataNascimento")
    private LocalDate dataNascimento;

    @ManyToOne
    @JoinColumn(name = "produtoId")
    private Produto produtoEscolhido;

    @Column(name = "dataSimulacao")
    private LocalDate dataSimulacao;

    @Column(name = "valorTotalPremio")
    private BigDecimal valorTotalPremio;

    public Simulacao() {
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

    public Produto getProdutoEscolhido() {
        return produtoEscolhido;
    }

    public void setProdutoEscolhido(Produto produtoEscolhido) {
        this.produtoEscolhido = produtoEscolhido;
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