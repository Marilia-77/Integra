package com.integra.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integra.demo.dto.LoginRequestDTO;
import com.integra.demo.dto.LoginResponseDTO;
import com.integra.demo.dto.ProfessorRequestDTO;
import com.integra.demo.dto.ProfessorResponseDTO;
import com.integra.demo.services.ProfessorService;

@RestController
@RequestMapping("/professores")
@CrossOrigin("*")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> cadastrar(@RequestBody ProfessorRequestDTO dto) {
        return ResponseEntity.ok(professorService.cadastrarProfessor(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(professorService.listarTodosProfessor());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.buscarPorIdProfessor(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> atualizar(@PathVariable Long id, @RequestBody ProfessorRequestDTO dto) {
        return ResponseEntity.ok(professorService.atualizarProfessor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        professorService.deletarProfessor(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ NOVO: Rota de Login do Professor
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(professorService.login(dto));
    }
}