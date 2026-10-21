package com.integra.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.integra.demo.entities.Materia;
import com.integra.demo.repositories.MateriaRepository;

@Service
public class MateriaService {

	  private final MateriaRepository materiaRepository;
	  
	  public MateriaService(MateriaRepository materiaRepository) {
	        this.materiaRepository = materiaRepository;
	    }

	  //cadastrar uma materia
	  public Materia cadastrarMateria(Materia materia) {
	        return materiaRepository.save(materia);
	    }

//Listar todas materias
	    public List<Materia> listarTodasMaterias() {
	        return materiaRepository.findAll();
	    }
	    
	    //buscar por id
	    public Materia buscarPorIdMateria(Long id) {
	        return materiaRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));
	    }

	  //atualizar
	    public Materia atualizar(Long id, Materia materia) {
	        Materia existente = buscarPorIdMateria(id);
	        existente.setNomeMateria(materia.getNomeMateria());
	        return materiaRepository.save(existente);
	    }
	    
	    //desisntgar
	    public void deletarMateria(Long id) {
			  materiaRepository.deleteById(id);
		    }
	    

}
