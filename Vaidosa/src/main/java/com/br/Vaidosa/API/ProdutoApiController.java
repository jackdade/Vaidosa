package com.br.Vaidosa.API;


import com.br.Vaidosa.Entidades.Produto;
import com.br.Vaidosa.Services.ProdutoService;
import com.br.Vaidosa.Views.ProdutoViewModel;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/Produto")
public class ProdutoApiController {

    @Autowired
    ProdutoService produtoService;

    @PostMapping("/inserir")
    public ResponseEntity<?> inserir(@RequestBody ProdutoViewModel produtoViewModel) {

        Produto existente = produtoService.buscarProdutoNome(produtoViewModel.getNome());
        if (existente != null) {
            return ResponseEntity.badRequest().body("Já existe um produto com esse nome!!");
        }

        try {
            produtoService.inserir(produtoViewModel);
            return ResponseEntity.status(201).body("Salvo com sucesso!");
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("Erro ao inserir");
        } catch (ValidationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarTodos() {
        try {
            return ResponseEntity.ok(produtoService.listarTodos());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Erro ao listar produtos");
        }
    }
}

