package com.integra.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.integra.demo.dto.LoginRequestDTO;
import com.integra.demo.dto.LoginResponseDTO;
import com.integra.demo.dto.ProfessorRequestDTO;
import com.integra.demo.dto.ProfessorResponseDTO;
import com.integra.demo.entities.Materia;
import com.integra.demo.entities.Professor;
import com.integra.demo.repositories.MateriaRepository;
import com.integra.demo.repositories.ProfessorRepository;
import com.integra.demo.security.JwtService;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final MateriaRepository materiaRepository;
    private final PasswordEncoder passwordEncoder; // ✅ Adicionado
    private final JwtService jwtService;           // ✅ Adicionado

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
        return new ProfessorResponseDTO(
                p.getIdProfessor(),
                p.getNomeProfessor(),
                p.getEmailProfessor(),
                p.getMateria() != null ? p.getMateria().getIdMateria() : null
        );
    }

    private void aplicar(Professor p, ProfessorRequestDTO dto) {
        p.setNomeProfessor(dto.nomeProfessor());
        p.setEmailProfessor(dto.emailProfessor());
        
        // ✅ Criptografa a senha ao cadastrar/atualizar
        p.setSenhaProfessor(passwordEncoder.encode(dto.senhaProfessor()));

        if (dto.idMateria() != null) {
            Materia m = materiaRepository.findById(dto.idMateria())
                    .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));
            p.setMateria(m);
        } else {
            p.setMateria(null);
        }
    }

    public ProfessorResponseDTO cadastrarProfessor(ProfessorRequestDTO dto) {
        Professor p = new Professor();
        aplicar(p, dto);
        return toResponse(professorRepository.save(p));
    }

    public List<ProfessorResponseDTO> listarTodosProfessor() {
        return professorRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ProfessorResponseDTO buscarPorIdProfessor(Long id) {
        Professor p = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        return toResponse(p);
    }

    public ProfessorResponseDTO atualizarProfessor(Long id, ProfessorRequestDTO dto) {
        Professor existente = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        aplicar(existente, dto);
        return toResponse(professorRepository.save(existente));
    }

    public void deletarProfessor(Long id) {
        professorRepository.deleteById(id);
    }

    // ✅ NOVO: Lógica de Login do Professor com JWT
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Professor p = professorRepository.findByEmailProfessor(dto.email())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha incorretos"));

        // Verifica se a senha digitada bate com o hash do banco
        if (!passwordEncoder.matches(dto.senha(), p.getSenhaProfessor())) {
            throw new RuntimeException("E-mail ou senha incorretos");
        }

        // Gera o Token com a role "PROFESSOR"
        String token = jwtService.gerarToken(p.getEmailProfessor(), "PROFESSOR");

        return new LoginResponseDTO(token, p.getEmailProfessor(), "PROFESSOR");
    }
}