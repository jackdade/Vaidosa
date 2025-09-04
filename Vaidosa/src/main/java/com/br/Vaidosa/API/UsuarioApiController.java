package com.br.Vaidosa.API;

import com.br.Vaidosa.Entidades.Usuario;
import com.br.Vaidosa.Repository.UsuarioRepository;
import com.br.Vaidosa.Services.UsuarioService;
import com.br.Vaidosa.Views.UsuarioViewModel;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioApiController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public List<UsuarioViewModel> index(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return usuarioService.buscaTodosPorPagina(pageable).stream().map(UsuarioViewModel::new).toList();
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(String login) {

        Usuario usuario = usuarioService.buscar(login);

        if (usuario != null) {
            return ResponseEntity.ok(new UsuarioViewModel(usuario));
        } else {
            return ResponseEntity.status(404).body("{\"mensagem\": \"Nenhum usuário encontrado\"}");
        }
    }

    @PostMapping("/inserir")
    public ResponseEntity<?> inserir(@RequestBody UsuarioViewModel usuarioVM) {

        Usuario existente = usuarioService.buscar(usuarioVM.getLogin());
        if (existente != null) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"Já existe um usuário com este login\"}");
        }

        try {
            Usuario usuario = usuarioService.inserir(usuarioVM);
            return ResponseEntity.status(201).body(new UsuarioViewModel(usuario));
        }catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"Já existe um registro com um valor único já cadastrado\"}");
        }
        catch (ValidationException ex) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"" + ex.getMessage() + "\"}");
        }
    }

    @PutMapping("/editar")
    public ResponseEntity<?> editar(@RequestBody  UsuarioViewModel usuarioVM) {



        if (usuarioVM.getId() == null) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"ID do usuário é obrigatório\"}");
        }

        Usuario usuario = usuarioRepository.findById(usuarioVM.getId()).orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(404).body("{\"mensagem\": \"Usuário não encontrado\"}");
        }

        try {
             usuarioService.atualizar(usuarioVM);
            return ResponseEntity.status(201).body(new UsuarioViewModel(usuario));
        }catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"Já existe um registro com um valor único já cadastrado\"}");
        }
        catch (ValidationException ex) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"" + ex.getMessage() + "\"}");
        }


    }

    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletar(@RequestParam String login) {

        Usuario usuario = usuarioService.buscar(login);
        if (usuario == null) {
            return ResponseEntity.status(404).body("{\"mensagem\": \"Usuário não encontrado\"}");
        }

        usuarioService.deletar(usuario.getId());
        return ResponseEntity.ok("{\"mensagem\": \"Usuário deletado com sucesso\"}");
    }
}
