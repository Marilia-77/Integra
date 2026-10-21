package com.integra.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integra.demo.dto.FrequenciaRequestDTO;
import com.integra.demo.dto.FrequenciaResponseDTO;
import com.integra.demo.services.FrequenciaService;

@RestController
@RequestMapping("/frequencia")
@CrossOrigin(origins = "*")
public class FrequenciaController {

    private final FrequenciaService frequenciaService;

    public FrequenciaController(FrequenciaService frequenciaService) {
        this.frequenciaService = frequenciaService;
    }

    @PostMapping
    public ResponseEntity<FrequenciaResponseDTO> cadastrar(@RequestBody FrequenciaRequestDTO dto) {
        return ResponseEntity.ok(frequenciaService.cadastrarFrequencia(dto));
    }

    @GetMapping
    public ResponseEntity<List<FrequenciaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(frequenciaService.listarTodasFrequencias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FrequenciaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(frequenciaService.buscarPorIdFrequencia(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FrequenciaResponseDTO> atualizar(@PathVariable Long id, @RequestBody FrequenciaRequestDTO dto) {
        return ResponseEntity.ok(frequenciaService.atualizarFrequencia(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        frequenciaService.deletarFrequencia(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/aluno/{idAluno}")
    public ResponseEntity<List<FrequenciaResponseDTO>> buscarPorAluno(@PathVariable Long idAluno) {
        return ResponseEntity.ok(frequenciaService.buscarPorAlunoFrequencia(idAluno));
    }

    @GetMapping("/aluno/{idAluno}/materia/{idMateria}")
    public ResponseEntity<FrequenciaResponseDTO> buscarPorAlunoEMateria(
            @PathVariable Long idAluno, @PathVariable Long idMateria) {
        return ResponseEntity.ok(frequenciaService.buscarPorAlunoEMateria(idAluno, idMateria));
    }

    @GetMapping("/verificar/{idAluno}")
    public ResponseEntity<Boolean> verificarFrequencia(@PathVariable Long idAluno) {
        return ResponseEntity.ok(frequenciaService.verificarFrequenciaRuim(idAluno));
    }

    @GetMapping("/ruins")
    public ResponseEntity<List<FrequenciaResponseDTO>> buscarFrequenciasRuins() {
        return ResponseEntity.ok(frequenciaService.buscarFrequenciasRuins());
    }
}