package com.br.Vaidosa.API;


import com.br.Vaidosa.Entidades.Pessoa;
import com.br.Vaidosa.Repository.PessoaRepository;
import com.br.Vaidosa.Services.PessoaService;
import com.br.Vaidosa.Views.PessoaViewModel;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
@CrossOrigin(origins = "http://localhost:5173") // Permite o frontend Vite
public class PessoaApiController {



    @Autowired
    private PessoaService pessoaService;


    @GetMapping
    public List<PessoaViewModel> index(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return pessoaService.buscaTodasPessoasPorPagina(pageable).stream().map(PessoaViewModel::new).toList();
    }


    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPessoa(@RequestParam(required = false) Long codigo, @RequestParam(required = false) String cpf) {

        Pessoa pessoa = pessoaService.buscarPessoa(codigo, cpf);

        if (pessoa != null) {
            PessoaViewModel pessoaVM = new PessoaViewModel(pessoa);
            return ResponseEntity.ok(pessoaVM);
        } else {
            return ResponseEntity.status(404).body("{\"mensagem\": \"Nenhum registro encontrado\"}");
        }
    }

    @PutMapping("/editar")
    public ResponseEntity<?> editarPessoa(@RequestBody PessoaViewModel pessoaVM) {



        if (pessoaVM.getCodigo() == null) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"Código da pessoa é obrigatório\"}");
        }

        Pessoa pessoa = pessoaService.buscarPessoa(pessoaVM.getCodigo(), null);

        if (pessoa == null) {
            return ResponseEntity.status(404).body("{\"mensagem\": \"Pessoa não encontrada\"}");
        }

        try {
            pessoaService.atualizar(pessoaVM);
            return ResponseEntity.status(201).body(new PessoaViewModel(pessoa));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"Já existe um registro com um valor único já cadastrado\"}");
        }
        catch (ValidationException ex) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"" + ex.getMessage() + "\"}");
        }


    }

    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletarPessoa(@RequestParam Long codigo) {

        Pessoa pessoa = pessoaService.buscarPessoa(codigo, null);

        if (pessoa == null) {
            return ResponseEntity.status(404).body("{\"mensagem\": \"Pessoa não encontrada\"}");
        }

        pessoaService.deletar(codigo);

        return ResponseEntity.ok("{\"mensagem\": \"Pessoa deletada com sucesso\"}");
    }

    @PostMapping("/inserir")
    public ResponseEntity<?> inserir(@RequestBody PessoaViewModel pessoaVM) {

        Pessoa existente = pessoaService.buscarPessoa(null, pessoaVM.getCpf());
        if (existente != null) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"Já existe uma pessoa com esse CPF\"}");
        }

        try {
            Pessoa pessoa = pessoaService.inserir(pessoaVM);
            return ResponseEntity.status(201).body(new PessoaViewModel(pessoa));
        }  catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("Já existe um registro com um valor igual ao campo: \"" +ex.getMessage());
        }
        catch (ValidationException ex) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"" + ex.getMessage() + "\"}");
        }
    }


}
