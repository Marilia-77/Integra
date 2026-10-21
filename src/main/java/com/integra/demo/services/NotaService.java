package com.integra.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.integra.demo.dto.NotaRequestDTO;
import com.integra.demo.dto.NotaResponseDTO;
import com.integra.demo.entities.Aluno;
import com.integra.demo.entities.Materia;
import com.integra.demo.entities.Nota;
import com.integra.demo.repositories.AlunoRepository;
import com.integra.demo.repositories.MateriaRepository;
import com.integra.demo.repositories.NotaRepository;

@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final MateriaRepository materiaRepository;

    public NotaService(NotaRepository notaRepository,
                       AlunoRepository alunoRepository,
                       MateriaRepository materiaRepository) {
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.materiaRepository = materiaRepository;
    }

    private void calcularMedia(Nota n) {
        n.setMedia((n.getNota1() + n.getNota2() + n.getNota3()) / 3);
    }

    private NotaResponseDTO toResponse(Nota n) {
        return new NotaResponseDTO(
                n.getIdNota(),
                n.getNota1(),
                n.getNota2(),
                n.getNota3(),
                n.getMedia(),
                n.getAluno() != null ? n.getAluno().getIdAluno() : null,
                n.getMateria() != null ? n.getMateria().getIdMateria() : null
        );
    }

    private void aplicar(Nota n, NotaRequestDTO dto) {
        n.setNota1(dto.nota1());
        n.setNota2(dto.nota2());
        n.setNota3(dto.nota3());

        if (dto.idAluno() != null) {
            Aluno a = alunoRepository.findById(dto.idAluno())
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
            n.setAluno(a);
        }
        if (dto.idMateria() != null) {
            Materia m = materiaRepository.findById(dto.idMateria())
                    .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));
            n.setMateria(m);
        }
        calcularMedia(n);
    }

    public NotaResponseDTO cadastrarNotas(NotaRequestDTO dto) {
        Nota n = new Nota();
        aplicar(n, dto);
        return toResponse(notaRepository.save(n));
    }

    public List<NotaResponseDTO> listarTodasNotas() {
        return notaRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public NotaResponseDTO buscarPorId(Long id) {
        Nota n = notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        return toResponse(n);
    }

    public NotaResponseDTO atualizarNota(Long id, NotaRequestDTO dto) {
        Nota existente = notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        aplicar(existente, dto);
        return toResponse(notaRepository.save(existente));
    }

    public void deletarNota(Long id) {
        notaRepository.deleteById(id);
    }

    public List<NotaResponseDTO> buscarPorAluno(Long idAluno) {
        return notaRepository.findByAlunoIdAluno(idAluno).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public NotaResponseDTO buscarPorAlunoEMateria(Long idAluno, Long idMateria) {
        Nota n = notaRepository.findByAlunoIdAlunoAndMateriaIdMateria(idAluno, idMateria)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        return toResponse(n);
    }

    public boolean verificarNotaCritica(Long idAluno) {
        for (Nota n : notaRepository.findByAlunoIdAluno(idAluno)) {
            if (n.getMedia() < 7) return true;
        }
        return false;
    }

    public List<NotaResponseDTO> buscarNotasCriticas() {
        return notaRepository.findByMediaLessThan(7.0).stream().map(this::toResponse).collect(Collectors.toList());
    }
}