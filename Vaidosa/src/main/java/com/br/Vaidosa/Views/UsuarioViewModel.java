package com.br.Vaidosa.Views;

import com.br.Vaidosa.Entidades.Pessoa;
import com.br.Vaidosa.Entidades.Usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class UsuarioViewModel {

    private Long id;

    private String login;

    private String senha;

    private Usuario.NivelAcesso tipoAcesso;

    private LocalDateTime dataCadastro;

    private LocalDateTime ultimoAcesso;

    private Boolean ativo;

    private Boolean bloqueado;

    private Pessoa pessoa;
    public UsuarioViewModel() {}


    public UsuarioViewModel(Usuario usuario) {
        this.id = usuario.getId();
        this.login = usuario.getLogin();
        this.tipoAcesso = usuario.getTipoAcesso();
        this.dataCadastro = usuario.getDataCadastro();
        this.ultimoAcesso = usuario.getUltimoAcesso();
        this.ativo = usuario.getAtivo();
        this.bloqueado = usuario.getBloqueado();

    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
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

    public Usuario.NivelAcesso getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(Usuario.NivelAcesso tipoAcesso) {
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


}
