package com.br.Vaidosa.Services;

import com.br.Vaidosa.Repository.SimulacaoRepository;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimulacaoService {

    @Autowired
    private Validator validator;

    @Autowired
    private SimulacaoRepository simulacaoRepository;


}
