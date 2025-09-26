package com.br.Vaidosa.Views;

import com.br.Vaidosa.Entidades.Produto;

import java.math.BigDecimal;

public class ProdutoViewModel {

    private Long id;
    private String nome;
    private Integer idadeMinima;
    private Integer idadeMaxima;
    private BigDecimal taxaJuros;
    private BigDecimal valorMinimoPremio;

    public ProdutoViewModel() {
    }

    public ProdutoViewModel(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.idadeMinima = produto.getIdadeMinima();
        this.idadeMaxima = produto.getIdadeMaxima();
        this.taxaJuros = produto.getTaxaJuros();
        this.valorMinimoPremio = produto.getValorMinimoPremio();
    }



    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdadeMinima() {
        return idadeMinima;
    }

    public void setIdadeMinima(Integer idadeMinima) {
        this.idadeMinima = idadeMinima;
    }

    public Integer getIdadeMaxima() {
        return idadeMaxima;
    }

    public void setIdadeMaxima(Integer idadeMaxima) {
        this.idadeMaxima = idadeMaxima;
    }

    public BigDecimal getTaxaJuros() {
        return taxaJuros;
    }

    public void setTaxaJuros(BigDecimal taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public BigDecimal getValorMinimoPremio() {
        return valorMinimoPremio;
    }

    public void setValorMinimoPremio(BigDecimal valorMinimoPremio) {
        this.valorMinimoPremio = valorMinimoPremio;
    }
}
