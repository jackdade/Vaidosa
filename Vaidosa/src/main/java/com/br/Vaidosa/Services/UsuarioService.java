package com.br.Vaidosa.Services;

import com.br.Vaidosa.Entidades.Pessoa;
import com.br.Vaidosa.Entidades.Usuario;
import com.br.Vaidosa.Repository.PessoaRepository;
import com.br.Vaidosa.Repository.UsuarioRepository;
import com.br.Vaidosa.Views.PessoaViewModel;
import com.br.Vaidosa.Views.UsuarioViewModel;
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
public class UsuarioService {

    @Autowired
    private Validator validator;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaRepository pessoaRepository;


    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Page<Usuario> buscaTodosPorPagina(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Usuario buscar(String login){
        Usuario usuario = new Usuario();


        usuario = usuarioRepository.findByLogin(login);


        return usuario;
    }

    public Usuario inserir(UsuarioViewModel usuarioVM) {



        Usuario usuario = new Usuario();
        usuario.setLogin(usuarioVM.getLogin());
        usuario.setSenha(usuarioVM.getSenha());
        usuario.setTipoAcesso(usuarioVM.getTipoAcesso());
        usuario.setPessoa(pessoaRepository.findByCodigo(usuarioVM.getPessoa().getCodigo()));
        usuario.setDataCadastro(LocalDateTime.now());
        usuario.setAtivo(usuarioVM.getAtivo() != null ? usuarioVM.getAtivo() : true);
        usuario.setBloqueado(usuarioVM.getBloqueado() != null ? usuarioVM.getBloqueado() : false);


        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        if (!violations.isEmpty()) {
            String erros = violations.stream().map(ConstraintViolation::getMessage).reduce((msg1, msg2) -> msg1 + "; " + msg2).orElse("Erro de validação");
            throw new ValidationException(erros);
        }


        return usuarioRepository.save(usuario);
    }


    public Usuario atualizar(UsuarioViewModel usuarioVM) {

        Usuario usuario = usuarioRepository.findById(usuarioVM.getId()).orElse(null);

        if (usuarioVM.getLogin() != null && !usuarioVM.getLogin().isEmpty()) {
            usuario.setLogin(usuarioVM.getLogin());
        }
        if (usuarioVM.getSenha() != null && !usuarioVM.getSenha().isEmpty()) {
            usuario.setSenha(usuarioVM.getSenha());
        }
        if (usuarioVM.getTipoAcesso() != null) {
            usuario.setTipoAcesso(usuarioVM.getTipoAcesso());
        }
        if (usuarioVM.getPessoa().getCodigo()!= null) {
            usuario.setPessoa(pessoaRepository.findByCodigo(usuarioVM.getPessoa().getCodigo()));
        }
        if (usuarioVM.getAtivo() != null) {
            usuario.setAtivo(usuarioVM.getAtivo());
        }
        if (usuarioVM.getBloqueado() != null) {
            usuario.setBloqueado(usuarioVM.getBloqueado());
        }
        if (usuarioVM.getUltimoAcesso() != null) {
            usuario.setUltimoAcesso(usuarioVM.getUltimoAcesso());
        }

        return usuarioRepository.save(usuario);
    }

    public void deletar(Long codigo) {
        usuarioRepository.deleteById(codigo);
    }


}
