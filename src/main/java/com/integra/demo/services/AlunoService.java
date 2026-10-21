package com.integra.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.integra.demo.dto.AlunoRequestDTO;
import com.integra.demo.dto.AlunoResponseDTO;
import com.integra.demo.entities.Aluno;
import com.integra.demo.entities.Turma;
import com.integra.demo.repositories.AlunoRepository;
import com.integra.demo.repositories.TurmaRepository;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public AlunoService(AlunoRepository alunoRepository, TurmaRepository turmaRepository) {
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    private AlunoResponseDTO toResponse(Aluno a) {
        return new AlunoResponseDTO(
                a.getIdAluno(),
                a.getNomeAluno(),
                a.getEmailAluno(),
                a.getDiagnosticoAluno(),
                a.getTurma() != null ? a.getTurma().getIdTurma() : null
        );
    }

    private void aplicar(Aluno a, AlunoRequestDTO dto) {
        a.setNomeAluno(dto.nomeAluno());
        a.setEmailAluno(dto.emailAluno());
        a.setSenhaAluno(dto.senhaAluno());
        a.setDiagnosticoAluno(dto.diagnosticoAluno());

        if (dto.idTurma() != null) {
            Turma t = turmaRepository.findById(dto.idTurma())
                    .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
            a.setTurma(t);
        } else {
            a.setTurma(null);
        }
    }

    public AlunoResponseDTO cadastrarAluno(AlunoRequestDTO dto) {
        Aluno a = new Aluno();
        aplicar(a, dto);
        return toResponse(alunoRepository.save(a));
    }

    public List<AlunoResponseDTO> listarTodosAlunos() {
        return alunoRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public AlunoResponseDTO buscarPorIdAluno(Long id) {
        Aluno a = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        return toResponse(a);
    }

    public AlunoResponseDTO atualizarAluno(Long id, AlunoRequestDTO dto) {
        Aluno existente = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        aplicar(existente, dto);
        return toResponse(alunoRepository.save(existente));
    }

    public void deletarAluno(Long id) {
        alunoRepository.deleteById(id);
    }

    public AlunoResponseDTO login(String email, String senha) {
        Aluno a = alunoRepository.findByEmailAlunoAndSenhaAluno(email, senha)
                .orElseThrow(() -> new RuntimeException("E-mail ou senha incorretos"));
        return toResponse(a);
    }

    public List<AlunoResponseDTO> listarAlunosPorTurma(Long idTurma) {
        Turma t = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        return alunoRepository.findByTurma(t).stream().map(this::toResponse).collect(Collectors.toList());
    }
}