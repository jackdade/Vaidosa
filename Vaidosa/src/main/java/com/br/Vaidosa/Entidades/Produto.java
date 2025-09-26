package com.br.Vaidosa.Entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "idadeMinima")
    private Integer idadeMinima;

    @Column(name = "idadeMaxima")
    private Integer idadeMaxima;

    @Column(name = "taxaJuros")
    private BigDecimal taxaJuros;

    @Column(name = "valorMinimoPremio")
    private BigDecimal valorMinimoPremio;

    public Produto() {
    }

    public Produto(String nome) {
        this.nome = nome;
    }

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
