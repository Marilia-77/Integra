package com.integra.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integra.demo.entities.Turma;
import com.integra.demo.services.TurmaService;

@RestController
@RequestMapping("/turma")
@CrossOrigin(origins = "*")
public class TurmaController {

	private final TurmaService turmaService;
	
	   public TurmaController(TurmaService turmaService) {
	        this.turmaService = turmaService;
	    }
	 
	   @PostMapping
	    public ResponseEntity<Turma> cadastrar(
	            @RequestBody Turma turma) {

	        return ResponseEntity.ok(
	                turmaService.cadastrarTurma(turma)
	        );
	    }

	   @GetMapping
	    public ResponseEntity<List<Turma>> listarTodas() {

	        return ResponseEntity.ok(
	                turmaService.listarTodasTurmas()
	        );
	    }
	   
	    @GetMapping("/{id}")
	    public ResponseEntity<Turma> buscarPorId(
	            @PathVariable Long id) {

	        return ResponseEntity.ok(
	                turmaService.buscarPorIdTurma(id)
	        );
	    }
	    
	    @PutMapping("/{id}")
	    public ResponseEntity<Turma> atualizar(
	            @PathVariable Long id,
	            @RequestBody Turma turma) {

	        return ResponseEntity.ok(
	                turmaService.atualizarTurma(id, turma)
	        );
	    }
	    
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deletar(
	            @PathVariable Long id) {

	        turmaService.deletarTurma(id);

	        return ResponseEntity.noContent().build();
	    }
	    
	

}
