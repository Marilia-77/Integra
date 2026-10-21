package com.integra.demo.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.integra.demo.dto.OcorrenciaRequestDTO;
import com.integra.demo.dto.OcorrenciaResponseDTO;
import com.integra.demo.entities.Aluno;
import com.integra.demo.entities.Ocorrencia;
import com.integra.demo.entities.Professor;
import com.integra.demo.repositories.AlunoRepository;
import com.integra.demo.repositories.OcorrenciaRepository;
import com.integra.demo.repositories.ProfessorRepository;

@Service
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;

    public OcorrenciaService(OcorrenciaRepository ocorrenciaRepository,
                             AlunoRepository alunoRepository,
                             ProfessorRepository professorRepository) {
        this.ocorrenciaRepository = ocorrenciaRepository;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
    }

    private OcorrenciaResponseDTO toResponse(Ocorrencia o) {
        return new OcorrenciaResponseDTO(
                o.getIdOcorrencia(),
                o.getTitulo(),
                o.getData(),
                o.getDescricao(),
                o.getCategoria(),
                o.getProfessor() != null ? o.getProfessor().getIdProfessor() : null,
                o.getAluno() != null ? o.getAluno().getIdAluno() : null
        );
    }

    private void aplicar(Ocorrencia o, OcorrenciaRequestDTO dto) {
        o.setTitulo(dto.titulo());
        o.setData(dto.data());
        o.setDescricao(dto.descricao());
        o.setCategoria(dto.categoria());

        if (dto.idProfessor() != null) {
            Professor p = professorRepository.findById(dto.idProfessor())
                    .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
            o.setProfessor(p);
        } else {
            o.setProfessor(null);
        }

        if (dto.idAluno() != null) {
            Aluno a = alunoRepository.findById(dto.idAluno())
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
            o.setAluno(a);
        } else {
            o.setAluno(null);
        }
    }

    public List<OcorrenciaResponseDTO> findAllOcorrencia() {
        return ocorrenciaRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public OcorrenciaResponseDTO findByIdOcorrencia(Long id) {
        Ocorrencia o = ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada"));
        return toResponse(o);
    }

    public OcorrenciaResponseDTO saveOcorrencia(OcorrenciaRequestDTO dto) {
        Ocorrencia o = new Ocorrencia();
        aplicar(o, dto);
        return toResponse(ocorrenciaRepository.save(o));
    }

    public OcorrenciaResponseDTO atualizarOcorrencia(Long id, OcorrenciaRequestDTO dto) {
        Ocorrencia existente = ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada"));
        aplicar(existente, dto);
        return toResponse(ocorrenciaRepository.save(existente));
    }

    public void deleteById(Long id) {
        ocorrenciaRepository.deleteById(id);
    }

    public List<OcorrenciaResponseDTO> findByIdAluno(Long idAluno) {
        return ocorrenciaRepository.findByAlunoIdAluno(idAluno).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<OcorrenciaResponseDTO> findByIdProfessor(Long idProfessor) {
        return ocorrenciaRepository.findByProfessorIdProfessor(idProfessor).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<OcorrenciaResponseDTO> findByCategoria(String categoria) {
        return ocorrenciaRepository.findByCategoria(categoria).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<OcorrenciaResponseDTO> findByDataBetween(LocalDate inicio, LocalDate fim) {
        return ocorrenciaRepository.findByDataBetween(inicio, fim).stream().map(this::toResponse).collect(Collectors.toList());
    }
}