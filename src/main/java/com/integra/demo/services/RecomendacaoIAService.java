package com.integra.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.integra.demo.dto.RecomendacaoIARequestDTO;
import com.integra.demo.dto.RecomendacaoIAResponseDTO;
import com.integra.demo.entities.Ocorrencia;
import com.integra.demo.entities.RecomendacaoIA;
import com.integra.demo.repositories.OcorrenciaRepository;
import com.integra.demo.repositories.RecomendacaoIARepository;

@Service
public class RecomendacaoIAService {

    private final RecomendacaoIARepository recomendacaoIARepository;
    private final OcorrenciaRepository ocorrenciaRepository;

    public RecomendacaoIAService(RecomendacaoIARepository recomendacaoIARepository,
                                 OcorrenciaRepository ocorrenciaRepository) {
        this.recomendacaoIARepository = recomendacaoIARepository;
        this.ocorrenciaRepository = ocorrenciaRepository;
    }

    private RecomendacaoIAResponseDTO toResponse(RecomendacaoIA r) {
        return new RecomendacaoIAResponseDTO(
                r.getIdRecomendacaoIA(),
                r.getTexto(),
                r.getOcorrencia() != null ? r.getOcorrencia().getIdOcorrencia() : null
        );
    }

    public RecomendacaoIAResponseDTO cadastrarRecomendacao(RecomendacaoIARequestDTO dto) {
        RecomendacaoIA r = new RecomendacaoIA();
        r.setTexto(dto.texto());

        if (dto.idOcorrencia() != null) {
            Ocorrencia o = ocorrenciaRepository.findById(dto.idOcorrencia())
                    .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada"));
            r.setOcorrencia(o);
        }
        return toResponse(recomendacaoIARepository.save(r));
    }

    public List<RecomendacaoIAResponseDTO> listarTodasRecomendacoes() {
        return recomendacaoIARepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public RecomendacaoIAResponseDTO buscarPorIdRecomendacao(Long id) {
        RecomendacaoIA r = recomendacaoIARepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recomendação não encontrada"));
        return toResponse(r);
    }

    public RecomendacaoIAResponseDTO atualizarRecomendacao(Long id, RecomendacaoIARequestDTO dto) {
        RecomendacaoIA existente = recomendacaoIARepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recomendação não encontrada"));

        existente.setTexto(dto.texto());

        if (dto.idOcorrencia() != null) {
            Ocorrencia o = ocorrenciaRepository.findById(dto.idOcorrencia())
                    .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada"));
            existente.setOcorrencia(o);
        } else {
            existente.setOcorrencia(null);
        }

        return toResponse(recomendacaoIARepository.save(existente));
    }
}