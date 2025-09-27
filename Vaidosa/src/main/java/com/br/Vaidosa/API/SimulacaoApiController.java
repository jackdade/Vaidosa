package com.br.Vaidosa.API;


import com.br.Vaidosa.Entidades.Produto;
import com.br.Vaidosa.Entidades.Simulacao;
import com.br.Vaidosa.Services.ProdutoService;
import com.br.Vaidosa.Services.SimulacaoService;
import com.br.Vaidosa.Views.ProdutoViewModel;
import com.br.Vaidosa.Views.SimulacaoViewModel;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Simulacao")
public class SimulacaoApiController {

    @Autowired
    SimulacaoService simulacaoService;

    @PostMapping("/simular")
    public ResponseEntity<?> simular(@RequestBody SimulacaoViewModel simulacaoViewModel) {

        try {

            simulacaoViewModel.setValorTotalPremio(simulacaoService.calcularPremio(simulacaoViewModel));

            Simulacao simulacao = simulacaoService.gravarSimulacao(simulacaoViewModel);


            return ResponseEntity.status(201).body(new SimulacaoViewModel(simulacao));
        }  catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("Erro ao inserrir");
        }
        catch (ValidationException ex) {
            return ResponseEntity.badRequest().body("{\"mensagem\": \"" + ex.getMessage() + "\"}");
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarTodos() {
        try {
            return ResponseEntity.ok(simulacaoService.listarTodos());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Erro ao listar produtos");
        }
    }

}
