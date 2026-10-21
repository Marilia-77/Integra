package com.integra.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integra.demo.dto.RecomendacaoIARequestDTO;
import com.integra.demo.dto.RecomendacaoIAResponseDTO;
import com.integra.demo.services.RecomendacaoIAService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/recomendacoes")
public class RecomendacaoIAController {

    private final RecomendacaoIAService recomendacaoIAService;

    public RecomendacaoIAController(RecomendacaoIAService recomendacaoIAService) {
        this.recomendacaoIAService = recomendacaoIAService;
    }

    @PostMapping
    public ResponseEntity<RecomendacaoIAResponseDTO> cadastrar(@RequestBody @Valid RecomendacaoIARequestDTO dto) {
        return ResponseEntity.ok(recomendacaoIAService.cadastrarRecomendacao(dto));
    }

    @PostMapping("/gerar/{idOcorrencia}")
    public ResponseEntity<RecomendacaoIAResponseDTO> gerarPorOcorrencia(@PathVariable Long idOcorrencia) {
        return ResponseEntity.ok(recomendacaoIAService.gerarRecomendacaoPorOcorrencia(idOcorrencia));
    }

    @GetMapping
    public ResponseEntity<List<RecomendacaoIAResponseDTO>> listarTodas() {
        return ResponseEntity.ok(recomendacaoIAService.listarTodasRecomendacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecomendacaoIAResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(recomendacaoIAService.buscarPorIdRecomendacao(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecomendacaoIAResponseDTO> atualizar(@PathVariable Long id,
                                                                @RequestBody @Valid RecomendacaoIARequestDTO dto) {
        return ResponseEntity.ok(recomendacaoIAService.atualizarRecomendacao(id, dto));
    }
}