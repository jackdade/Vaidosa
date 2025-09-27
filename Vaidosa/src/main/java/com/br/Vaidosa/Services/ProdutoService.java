package com.br.Vaidosa.Services;

import com.br.Vaidosa.Entidades.Produto;
import com.br.Vaidosa.Repository.ProdutoRepository;
import com.br.Vaidosa.Views.ProdutoViewModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ProdutoService {

    @Autowired
    private Validator validator;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto buscarProdutoNome(String nome){
        Produto produto = new Produto();
        produto = produtoRepository.findFirstByNome(nome);

        return produto;
    }

    public Produto inserir(ProdutoViewModel produtoViewModel) {

        Produto produto = new Produto();
        produto.setNome(produtoViewModel.getNome());
        produto.setIdadeMinima(produtoViewModel.getIdadeMinima());
        produto.setIdadeMaxima(produtoViewModel.getIdadeMaxima());
        produto.setTaxaJuros(produtoViewModel.getTaxaJuros());

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        if (!violations.isEmpty()) {
            String erros = violations.stream().map(ConstraintViolation::getMessage).reduce((msg1, msg2) -> msg1 + "; " + msg2).orElse("Erro de validação");
            throw new ValidationException(erros);
        }


        return produtoRepository.save(produto);
    }

    public List<ProdutoViewModel> listarTodos() {
        List<Produto> produtos = produtoRepository.findAll();

        return produtos.stream()
                .map(ProdutoViewModel::new)
                .toList();
    }


}
