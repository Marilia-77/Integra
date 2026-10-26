package com.integra.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integra.demo.dto.LoginRequestDTO;
import com.integra.demo.dto.LoginResponseDTO;
import com.integra.demo.dto.ProfessorRequestDTO;
import com.integra.demo.dto.ProfessorResponseDTO;
import com.integra.demo.entities.Materia;
import com.integra.demo.entities.Professor;
import com.integra.demo.exception.ConflictException;
import com.integra.demo.exception.NotFoundException;
import com.integra.demo.exception.UnauthorizedException;
import com.integra.demo.exception.ValidationException;
import com.integra.demo.repositories.MateriaRepository;
import com.integra.demo.repositories.ProfessorRepository;
import com.integra.demo.security.JwtService;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final MateriaRepository materiaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ProfessorService(ProfessorRepository professorRepository,
                            MateriaRepository materiaRepository,
                            PasswordEncoder passwordEncoder,
                            JwtService jwtService) {
        this.professorRepository = professorRepository;
        this.materiaRepository = materiaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    private ProfessorResponseDTO toResponse(Professor p) {
        Long idMateria = null;
        if (p.getMateria() != null) {
            idMateria = p.getMateria().getIdMateria();
        }
        return new ProfessorResponseDTO(
                p.getIdProfessor(),
                p.getNomeProfessor(),
                p.getEmailProfessor(),
                idMateria
        );
    }

    private void aplicar(Professor p, ProfessorRequestDTO dto, boolean isNovo) {
        p.setNomeProfessor(dto.nomeProfessor());
        p.setEmailProfessor(dto.emailProfessor());

        if (isNovo) {
            if (dto.senhaProfessor() == null || dto.senhaProfessor().isBlank()) {
                throw new ValidationException("Senha é obrigatória no cadastro");
            }
            p.setSenhaProfessor(passwordEncoder.encode(dto.senhaProfessor()));
        } else if (dto.senhaProfessor() != null && !dto.senhaProfessor().isBlank()) {
            p.setSenhaProfessor(passwordEncoder.encode(dto.senhaProfessor()));
        }

        if (dto.idMateria() != null) {
            Materia m = materiaRepository.findById(dto.idMateria())
                    .orElseThrow(() -> new NotFoundException("Matéria não encontrada"));
            p.setMateria(m);
        } else {
            p.setMateria(null);
        }
    }

    public ProfessorResponseDTO cadastrarProfessor(ProfessorRequestDTO dto) {
        if (professorRepository.findByEmailProfessor(dto.emailProfessor()).isPresent()) {
            throw new ConflictException("E-mail já cadastrado");
        }
        Professor p = new Professor();
        aplicar(p, dto, true);
        return toResponse(professorRepository.save(p));
    }

    @Transactional(readOnly = true)
    public List<ProfessorResponseDTO> listarTodosProfessor() {
        return professorRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfessorResponseDTO buscarPorIdProfessor(Long id) {
        Professor p = professorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Professor não encontrado"));
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public ProfessorResponseDTO buscarPorEmail(String email) {
        Professor p = professorRepository.findByEmailProfessor(email)
                .orElseThrow(() -> new NotFoundException("Professor não encontrado"));
        return toResponse(p);
    }

    public ProfessorResponseDTO atualizarProfessor(Long id, ProfessorRequestDTO dto) {
        Professor existente = professorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Professor não encontrado"));
        aplicar(existente, dto, false);
        return toResponse(professorRepository.save(existente));
    }

    public void deletarProfessor(Long id) {
        professorRepository.deleteById(id);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Professor p = professorRepository.findByEmailProfessor(dto.email())
                .orElseThrow(() -> new UnauthorizedException("E-mail ou senha incorretos"));

        if (!passwordEncoder.matches(dto.senha(), p.getSenhaProfessor())) {
            throw new UnauthorizedException("E-mail ou senha incorretos");
        }

        String token = jwtService.gerarToken(p.getEmailProfessor(), "PROFESSOR");
        return new LoginResponseDTO(token, p.getEmailProfessor(), "PROFESSOR");
    }
}