package com.integra.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.integra.demo.entities.Materia;
import com.integra.demo.exception.NotFoundException;
import com.integra.demo.repositories.MateriaRepository;

@Service
public class MateriaService {

    private final MateriaRepository materiaRepository;

    public MateriaService(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    public Materia cadastrarMateria(Materia materia) {
        return materiaRepository.save(materia);
    }

    public List<Materia> listarTodasMaterias() {
        return materiaRepository.findAll();
    }

    public Materia buscarPorIdMateria(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Matéria não encontrada"));
    }

    public Materia atualizar(Long id, Materia materia) {
        Materia existente = buscarPorIdMateria(id);
        existente.setNomeMateria(materia.getNomeMateria());
        return materiaRepository.save(existente);
    }

    public void deletarMateria(Long id) {
        materiaRepository.deleteById(id);
    }
}