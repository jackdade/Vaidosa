package com.br.Vaidosa.Entidades;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O login não pode ser vazio")
    @Size(min = 3, max = 50, message = "O login deve ter entre 3 e 50 caracteres")
    @Column(name = "login", nullable = false, unique = true, length = 50)
    private String login;

    @NotBlank(message = "A senha não pode ser vazia")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Column(name = "senha", nullable = false)
    private String senha;

    @NotNull(message = "O tipo de acesso é obrigatório")
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipoAcesso", nullable = false)
    private NivelAcesso tipoAcesso = NivelAcesso.CLIENTE;

    @NotNull(message = "A data de cadastro é obrigatória")
    @PastOrPresent(message = "A data de cadastro não pode ser no futuro")
    @Column(name = "dataCadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @PastOrPresent(message = "O último acesso não pode ser no futuro")
    @Column(name = "ultimoAcesso")
    private LocalDateTime ultimoAcesso;

    @NotNull(message = "O campo ativo é obrigatório")
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @NotNull(message = "O campo bloqueado é obrigatório")
    @Column(name = "bloqueado", nullable = false)
    private Boolean bloqueado = false;

    @NotNull(message = "A pessoa vinculada é obrigatória")
    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
    private Pessoa pessoa;



    public enum NivelAcesso {
        PROPRIETARIO(1, "Proprietário"),
        GERENTE(2, "Gerente"),
        TRABALHADOR(3, "Trabalhador"),
        CLIENTE(4, "Cliente");

        private final int codigo;
        private final String descricao;

        NivelAcesso(int codigo, String descricao) {
            this.codigo = codigo;
            this.descricao = descricao;
        }

        public int getCodigo() {
            return codigo;
        }

        public String getDescricao() {
            return descricao;
        }

        @Override
        public String toString() {
            return codigo + " - " + descricao;
        }
    }


    public Usuario() {
        this.dataCadastro = LocalDateTime.now();
    }

    public Usuario(String login, String senha, Pessoa pessoa) {
        this();
        this.login = login;
        this.senha = senha;
        this.pessoa = pessoa;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public NivelAcesso getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(NivelAcesso tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }


    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }



}