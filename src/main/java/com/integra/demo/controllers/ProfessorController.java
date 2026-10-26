package com.integra.demo.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integra.demo.dto.LoginRequestDTO;
import com.integra.demo.dto.LoginResponseDTO;
import com.integra.demo.dto.ProfessorRequestDTO;
import com.integra.demo.dto.ProfessorResponseDTO;
import com.integra.demo.services.ProfessorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> cadastrar(@RequestBody @Valid ProfessorRequestDTO dto) {
        return ResponseEntity.ok(professorService.cadastrarProfessor(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(professorService.listarTodosProfessor());
    }

    @GetMapping("/me")
    public ResponseEntity<ProfessorResponseDTO> buscarMeuPerfil(Principal principal) {
        return ResponseEntity.ok(professorService.buscarPorEmail(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.buscarPorIdProfessor(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> atualizar(@PathVariable Long id,
                                                           @RequestBody @Valid ProfessorRequestDTO dto) {
        return ResponseEntity.ok(professorService.atualizarProfessor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        professorService.deletarProfessor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(professorService.login(dto));
    }
}