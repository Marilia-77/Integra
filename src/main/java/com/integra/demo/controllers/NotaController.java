package com.integra.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integra.demo.dto.NotaRequestDTO;
import com.integra.demo.dto.NotaResponseDTO;
import com.integra.demo.services.NotaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/nota")
public class NotaController {

    private final NotaService notaService;

    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    @PostMapping
    public ResponseEntity<NotaResponseDTO> cadastrar(@RequestBody @Valid NotaRequestDTO dto) {
        return ResponseEntity.ok(notaService.cadastrarNotas(dto));
    }

    @GetMapping
    public ResponseEntity<List<NotaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(notaService.listarTodasNotas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaResponseDTO> atualizar(@PathVariable Long id,
                                                      @RequestBody @Valid NotaRequestDTO dto) {
        return ResponseEntity.ok(notaService.atualizarNota(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        notaService.deletarNota(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/aluno/{idAluno}")
    public ResponseEntity<List<NotaResponseDTO>> buscarPorAluno(@PathVariable Long idAluno) {
        return ResponseEntity.ok(notaService.buscarPorAluno(idAluno));
    }

    @GetMapping("/aluno/{idAluno}/materia/{idMateria}")
    public ResponseEntity<NotaResponseDTO> buscarPorAlunoEMateria(
            @PathVariable Long idAluno, @PathVariable Long idMateria) {
        return ResponseEntity.ok(notaService.buscarPorAlunoEMateria(idAluno, idMateria));
    }

    @GetMapping("/verificar/{idAluno}")
    public ResponseEntity<Boolean> verificarNotaCritica(@PathVariable Long idAluno) {
        return ResponseEntity.ok(notaService.verificarNotaCritica(idAluno));
    }

    @GetMapping("/criticas")
    public ResponseEntity<List<NotaResponseDTO>> buscarNotasCriticas() {
        return ResponseEntity.ok(notaService.buscarNotasCriticas());
    }
}