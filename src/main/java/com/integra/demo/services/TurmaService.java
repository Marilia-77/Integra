package com.integra.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.integra.demo.entities.Turma;
import com.integra.demo.exception.NotFoundException;
import com.integra.demo.repositories.TurmaRepository;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;

    public TurmaService(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    public Turma cadastrarTurma(Turma turma) {
        return turmaRepository.save(turma);
    }

    public List<Turma> listarTodasTurmas() {
        return turmaRepository.findAll();
    }

    public Turma buscarPorIdTurma(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Turma não encontrada"));
    }

    public Turma atualizarTurma(Long idTurma, Turma turma) {
        Turma existente = buscarPorIdTurma(idTurma);
        existente.setAno(turma.getAno());
        existente.setGrau(turma.getGrau());
        return turmaRepository.save(existente);
    }

    public void deletarTurma(Long id) {
        turmaRepository.deleteById(id);
    }
}