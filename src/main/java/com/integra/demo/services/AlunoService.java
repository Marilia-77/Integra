package com.integra.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integra.demo.dto.AlunoRequestDTO;
import com.integra.demo.dto.AlunoResponseDTO;
import com.integra.demo.dto.LoginRequestDTO;
import com.integra.demo.dto.LoginResponseDTO;
import com.integra.demo.entities.Aluno;
import com.integra.demo.entities.Turma;
import com.integra.demo.exception.ConflictException;
import com.integra.demo.exception.NotFoundException;
import com.integra.demo.exception.UnauthorizedException;
import com.integra.demo.exception.ValidationException;
import com.integra.demo.repositories.AlunoRepository;
import com.integra.demo.repositories.TurmaRepository;
import com.integra.demo.security.JwtService;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AlunoService(AlunoRepository alunoRepository,
                        TurmaRepository turmaRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService) {
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

    private void aplicar(Aluno a, AlunoRequestDTO dto, boolean isNovo) {
        a.setNomeAluno(dto.nomeAluno());
        a.setEmailAluno(dto.emailAluno());
        a.setDiagnosticoAluno(dto.diagnosticoAluno());

        if (isNovo) {
            if (dto.senhaAluno() == null || dto.senhaAluno().isBlank()) {
                throw new ValidationException("Senha é obrigatória no cadastro");
            }
            a.setSenhaAluno(passwordEncoder.encode(dto.senhaAluno()));
        } else if (dto.senhaAluno() != null && !dto.senhaAluno().isBlank()) {
            a.setSenhaAluno(passwordEncoder.encode(dto.senhaAluno()));
        }

        if (dto.idTurma() != null) {
            Turma t = turmaRepository.findById(dto.idTurma())
                    .orElseThrow(() -> new NotFoundException("Turma não encontrada"));
            a.setTurma(t);
        } else {
            a.setTurma(null);
        }
    }

    public AlunoResponseDTO cadastrarAluno(AlunoRequestDTO dto) {
        if (alunoRepository.findByEmailAluno(dto.emailAluno()).isPresent()) {
            throw new ConflictException("E-mail já cadastrado");
        }
        Aluno a = new Aluno();
        aplicar(a, dto, true);
        return toResponse(alunoRepository.save(a));
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> listarTodosAlunos() {
        return alunoRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AlunoResponseDTO buscarPorIdAluno(Long id) {
        Aluno a = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
        return toResponse(a);
    }

    @Transactional(readOnly = true)
    public AlunoResponseDTO buscarPorEmail(String email) {
        Aluno a = alunoRepository.findByEmailAluno(email)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
        return toResponse(a);
    }

    public AlunoResponseDTO atualizarAluno(Long id, AlunoRequestDTO dto) {
        Aluno existente = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
        aplicar(existente, dto, false);
        return toResponse(alunoRepository.save(existente));
    }

    public void deletarAluno(Long id) {
        alunoRepository.deleteById(id);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Aluno a = alunoRepository.findByEmailAluno(dto.email())
                .orElseThrow(() -> new UnauthorizedException("E-mail ou senha incorretos"));

        if (!passwordEncoder.matches(dto.senha(), a.getSenhaAluno())) {
            throw new UnauthorizedException("E-mail ou senha incorretos");
        }

        String token = jwtService.gerarToken(a.getEmailAluno(), "ALUNO");
        return new LoginResponseDTO(token, a.getEmailAluno(), "ALUNO");
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> listarAlunosPorTurma(Long idTurma) {
        Turma t = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new NotFoundException("Turma não encontrada"));
        return alunoRepository.findByTurma(t).stream().map(this::toResponse).collect(Collectors.toList());
    }
}