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

import com.integra.demo.entities.Materia;
import com.integra.demo.services.MateriaService;

@RestController
@RequestMapping("/materia")
@CrossOrigin(origins = "*")
public class MateriaController {

	private final MateriaService materiaService;
	
    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @PostMapping
    public ResponseEntity<Materia> cadastrar(
            @RequestBody Materia materia) {

        return ResponseEntity.ok(
                materiaService.cadastrarMateria(materia)
        );
    }
    
    @GetMapping
    public ResponseEntity<List<Materia>> listarTodas() {

        return ResponseEntity.ok(
                materiaService.listarTodasMaterias()
        );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Materia> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                materiaService.buscarPorIdMateria(id)
        );
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Materia> atualizar(
            @PathVariable Long id,
            @RequestBody Materia materia) {

        return ResponseEntity.ok(
                materiaService.atualizar(id, materia)
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        materiaService.deletarMateria(id);

        return ResponseEntity.noContent().build();
    }
    
    
    
    
	
	
}
