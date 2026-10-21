package com.integra.demo.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integra.demo.dto.OcorrenciaRequestDTO;
import com.integra.demo.dto.OcorrenciaResponseDTO;
import com.integra.demo.services.OcorrenciaService;

@RestController
@RequestMapping("/ocorrencia")
@CrossOrigin(origins = "*")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    public OcorrenciaController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    @GetMapping
    public ResponseEntity<List<OcorrenciaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(ocorrenciaService.findAllOcorrencia());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ocorrenciaService.findByIdOcorrencia(id));
    }

    @PostMapping
    public ResponseEntity<OcorrenciaResponseDTO> cadastrar(@RequestBody OcorrenciaRequestDTO dto) {
        return ResponseEntity.ok(ocorrenciaService.saveOcorrencia(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> atualizar(@PathVariable Long id, @RequestBody OcorrenciaRequestDTO dto) {
        return ResponseEntity.ok(ocorrenciaService.atualizarOcorrencia(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ocorrenciaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/aluno/{idAluno}")
    public ResponseEntity<List<OcorrenciaResponseDTO>> buscarPorAluno(@PathVariable Long idAluno) {
        return ResponseEntity.ok(ocorrenciaService.findByIdAluno(idAluno));
    }

    @GetMapping("/professor/{idProfessor}")
    public ResponseEntity<List<OcorrenciaResponseDTO>> buscarPorProfessor(@PathVariable Long idProfessor) {
        return ResponseEntity.ok(ocorrenciaService.findByIdProfessor(idProfessor));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<OcorrenciaResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(ocorrenciaService.findByCategoria(categoria));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<OcorrenciaResponseDTO>> buscarPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(ocorrenciaService.findByDataBetween(inicio, fim));
    }
}