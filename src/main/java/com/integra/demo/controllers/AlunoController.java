package com.integra.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integra.demo.dto.AlunoRequestDTO;
import com.integra.demo.dto.AlunoResponseDTO;
import com.integra.demo.services.AlunoService;

@RestController
@RequestMapping("/aluno")
@CrossOrigin(origins = "*")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> cadastrar(@RequestBody AlunoRequestDTO dto) {
        return ResponseEntity.ok(alunoService.cadastrarAluno(dto));
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(alunoService.listarTodosAlunos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.buscarPorIdAluno(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizar(@PathVariable Long id, @RequestBody AlunoRequestDTO dto) {
        return ResponseEntity.ok(alunoService.atualizarAluno(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        alunoService.deletarAluno(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AlunoResponseDTO> login(
            @RequestParam("emailAluno") String emailAluno,
            @RequestParam("senhaAluno") String senhaAluno) {
        return ResponseEntity.ok(alunoService.login(emailAluno, senhaAluno));
    }

    @PostMapping("/porTurma")
    public ResponseEntity<List<AlunoResponseDTO>> listarPorTurma(@RequestParam("idTurma") Long idTurma) {
        return ResponseEntity.ok(alunoService.listarAlunosPorTurma(idTurma));
    }
}