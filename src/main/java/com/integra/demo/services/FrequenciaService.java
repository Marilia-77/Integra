package com.integra.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.integra.demo.dto.FrequenciaRequestDTO;
import com.integra.demo.dto.FrequenciaResponseDTO;
import com.integra.demo.entities.Aluno;
import com.integra.demo.entities.Frequencia;
import com.integra.demo.entities.Materia;
import com.integra.demo.exception.NotFoundException;
import com.integra.demo.repositories.AlunoRepository;
import com.integra.demo.repositories.FrequenciaRepository;
import com.integra.demo.repositories.MateriaRepository;

@Service
public class FrequenciaService {

    private final FrequenciaRepository frequenciaRepository;
    private final AlunoRepository alunoRepository;
    private final MateriaRepository materiaRepository;

    public FrequenciaService(FrequenciaRepository frequenciaRepository,
                             AlunoRepository alunoRepository,
                             MateriaRepository materiaRepository) {
        this.frequenciaRepository = frequenciaRepository;
        this.alunoRepository = alunoRepository;
        this.materiaRepository = materiaRepository;
    }

    private FrequenciaResponseDTO toResponse(Frequencia f) {
        return new FrequenciaResponseDTO(
                f.getIdFrequencia(),
                f.getPorcentagem(),
                f.getAluno() != null ? f.getAluno().getIdAluno() : null,
                f.getMateria() != null ? f.getMateria().getIdMateria() : null
        );
    }

    private void aplicar(Frequencia f, FrequenciaRequestDTO dto) {
        f.setPorcentagem(dto.porcentagem());

        if (dto.idAluno() != null) {
            Aluno a = alunoRepository.findById(dto.idAluno())
                    .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
            f.setAluno(a);
        }
        if (dto.idMateria() != null) {
            Materia m = materiaRepository.findById(dto.idMateria())
                    .orElseThrow(() -> new NotFoundException("Matéria não encontrada"));
            f.setMateria(m);
        }
    }

    public FrequenciaResponseDTO cadastrarFrequencia(FrequenciaRequestDTO dto) {
        Frequencia f = new Frequencia();
        aplicar(f, dto);
        return toResponse(frequenciaRepository.save(f));
    }

    public List<FrequenciaResponseDTO> listarTodasFrequencias() {
        return frequenciaRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public FrequenciaResponseDTO buscarPorIdFrequencia(Long id) {
        Frequencia f = frequenciaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Frequência não encontrada"));
        return toResponse(f);
    }

    public FrequenciaResponseDTO atualizarFrequencia(Long id, FrequenciaRequestDTO dto) {
        Frequencia existente = frequenciaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Frequência não encontrada"));
        aplicar(existente, dto);
        return toResponse(frequenciaRepository.save(existente));
    }

    public void deletarFrequencia(Long id) {
        frequenciaRepository.deleteById(id);
    }

    public List<FrequenciaResponseDTO> buscarPorAlunoFrequencia(Long idAluno) {
        return frequenciaRepository.findByAlunoIdAluno(idAluno)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public FrequenciaResponseDTO buscarPorAlunoEMateria(Long idAluno, Long idMateria) {
        Frequencia f = frequenciaRepository.findByAlunoIdAlunoAndMateriaIdMateria(idAluno, idMateria)
                .orElseThrow(() -> new NotFoundException("Frequência não encontrada"));
        return toResponse(f);
    }

    public boolean verificarFrequenciaRuim(Long idAluno) {
        for (Frequencia f : frequenciaRepository.findByAlunoIdAluno(idAluno)) {
            if (f.getPorcentagem() < 70.0) return true;
        }
        return false;
    }

    public List<FrequenciaResponseDTO> buscarFrequenciasRuins() {
        return frequenciaRepository.findByPorcentagemLessThan(70.0)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
}