package com.integra.demo.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integra.demo.entities.Aluno;
import com.integra.demo.entities.Professor;
import com.integra.demo.entities.Turma;
import com.integra.demo.exception.NotFoundException;
import com.integra.demo.repositories.AlunoRepository;
import com.integra.demo.repositories.ProfessorRepository;
import com.integra.demo.repositories.TurmaRepository;

@Service
public class TurmaService {

    private static final Logger log = LoggerFactory.getLogger(TurmaService.class);

    private final TurmaRepository turmaRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;

    public TurmaService(TurmaRepository turmaRepository, 
                        ProfessorRepository professorRepository,
                        AlunoRepository alunoRepository) {
        this.turmaRepository = turmaRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
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

    @Transactional
    public Turma adicionarProfessor(Long idTurma, Long idProfessor) {
        log.info("Adicionando professor {} na turma {}", idProfessor, idTurma);
        Turma turma = buscarPorIdTurma(idTurma);
        Professor prof = professorRepository.findById(idProfessor)
                .orElseThrow(() -> new NotFoundException("Professor não encontrado"));

        if (turma.getProfessores().stream().noneMatch(p -> p.getIdProfessor().equals(idProfessor))) {
            turma.getProfessores().add(prof);
        }
        Turma salva = turmaRepository.saveAndFlush(turma);
        return salva;
    }

    @Transactional
    public Turma removerProfessor(Long idTurma, Long idProfessor) {
        Turma turma = buscarPorIdTurma(idTurma);
        turma.getProfessores().removeIf(p -> p.getIdProfessor().equals(idProfessor));
        return turmaRepository.saveAndFlush(turma);
    }

    @Transactional
    public Turma adicionarAluno(Long idTurma, Long idAluno) {
        Turma turma = buscarPorIdTurma(idTurma);
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        aluno.setTurma(turma);
        alunoRepository.save(aluno);
        
        return turma;
    }

    @Transactional
    public Turma removerAluno(Long idTurma, Long idAluno) {
        Turma turma = buscarPorIdTurma(idTurma);
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        if (aluno.getTurma() != null && aluno.getTurma().getIdTurma().equals(idTurma)) {
            aluno.setTurma(null);
            alunoRepository.save(aluno);
        }
        
        return turma;
    }
}