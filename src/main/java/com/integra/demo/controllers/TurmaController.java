package com.integra.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integra.demo.entities.Turma;
import com.integra.demo.services.TurmaService;

@RestController
@RequestMapping("/turma")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @PostMapping
    public ResponseEntity<Turma> cadastrar(@RequestBody Turma turma) {
        return ResponseEntity.ok(turmaService.cadastrarTurma(turma));
    }

    @GetMapping
    public ResponseEntity<List<Turma>> listarTodas() {
        return ResponseEntity.ok(turmaService.listarTodasTurmas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turma> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(turmaService.buscarPorIdTurma(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Turma> atualizar(@PathVariable Long id, @RequestBody Turma turma) {
        return ResponseEntity.ok(turmaService.atualizarTurma(id, turma));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        turmaService.deletarTurma(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idTurma}/professores/{idProfessor}")
    public ResponseEntity<Turma> adicionarProfessor(@PathVariable Long idTurma, @PathVariable Long idProfessor) {
        return ResponseEntity.ok(turmaService.adicionarProfessor(idTurma, idProfessor));
    }

    @DeleteMapping("/{idTurma}/professores/{idProfessor}")
    public ResponseEntity<Turma> removerProfessor(@PathVariable Long idTurma, @PathVariable Long idProfessor) {
        return ResponseEntity.ok(turmaService.removerProfessor(idTurma, idProfessor));
    }

    // NOVAS ROTAS PARA ALUNOS
    @PostMapping("/{idTurma}/alunos/{idAluno}")
    public ResponseEntity<Turma> adicionarAluno(@PathVariable Long idTurma, @PathVariable Long idAluno) {
        return ResponseEntity.ok(turmaService.adicionarAluno(idTurma, idAluno));
    }

    @DeleteMapping("/{idTurma}/alunos/{idAluno}")
    public ResponseEntity<Turma> removerAluno(@PathVariable Long idTurma, @PathVariable Long idAluno) {
        return ResponseEntity.ok(turmaService.removerAluno(idTurma, idAluno));
    }
}