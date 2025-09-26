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
            return ResponseEntity.badRequest().body("{\"mensagem\": \"Já existe um produto com esse nome!!\"}");
        }

        try {
            Produto produto = produtoService.inserir(produtoViewModel);
            return ResponseEntity.status(201).body(new ProdutoViewModel(produto));
        }  catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("Erro ao inserrir");
        }
        catch (ValidationException ex) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"" + ex.getMessage() + "\"}");
        }
    }


}
