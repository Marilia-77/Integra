package com.integra.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integra.demo.dto.RecomendacaoIARequestDTO;
import com.integra.demo.dto.RecomendacaoIAResponseDTO;
import com.integra.demo.entities.Ocorrencia;
import com.integra.demo.entities.RecomendacaoIA;
import com.integra.demo.exception.NotFoundException;
import com.integra.demo.repositories.OcorrenciaRepository;
import com.integra.demo.repositories.RecomendacaoIARepository;

@Service
public class RecomendacaoIAService {

    private final RecomendacaoIARepository recomendacaoIARepository;
    private final OcorrenciaRepository ocorrenciaRepository;
    private final NvidiaAIService nvidiaAIService;

    public RecomendacaoIAService(RecomendacaoIARepository recomendacaoIARepository,
                                 OcorrenciaRepository ocorrenciaRepository,
                                 NvidiaAIService nvidiaAIService) {
        this.recomendacaoIARepository = recomendacaoIARepository;
        this.ocorrenciaRepository = ocorrenciaRepository;
        this.nvidiaAIService = nvidiaAIService;
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
                    .orElseThrow(() -> new NotFoundException("Ocorrência não encontrada"));
            r.setOcorrencia(o);
        }
        return toResponse(recomendacaoIARepository.save(r));
    }

    @Transactional
    public RecomendacaoIAResponseDTO gerarRecomendacaoPorOcorrencia(Long idOcorrencia) {
        Ocorrencia ocorrencia = ocorrenciaRepository.findById(idOcorrencia)
                .orElseThrow(() -> new NotFoundException("Ocorrência não encontrada"));

        String prompt = """
                Você é um assistente pedagógico especializado em educação inclusiva.
                Com base na ocorrência abaixo, sugira uma intervenção prática e acolhedora para o professor.

                Título: %s
                Categoria: %s
                Descrição: %s

                Recomendação (seja direto e prático, máximo 3 frases):
                """.formatted(
                ocorrencia.getTitulo(),
                ocorrencia.getCategoria(),
                ocorrencia.getDescricao()
        );

        String textoGerado = nvidiaAIService.gerarRecomendacao(prompt);

        RecomendacaoIA recomendacao = new RecomendacaoIA();
        recomendacao.setTexto(textoGerado);
        recomendacao.setOcorrencia(ocorrencia);

        return toResponse(recomendacaoIARepository.save(recomendacao));
    }

    public List<RecomendacaoIAResponseDTO> listarTodasRecomendacoes() {
        return recomendacaoIARepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public RecomendacaoIAResponseDTO buscarPorIdRecomendacao(Long id) {
        RecomendacaoIA r = recomendacaoIARepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recomendação não encontrada"));
        return toResponse(r);
    }

    public RecomendacaoIAResponseDTO atualizarRecomendacao(Long id, RecomendacaoIARequestDTO dto) {
        RecomendacaoIA existente = recomendacaoIARepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recomendação não encontrada"));

        existente.setTexto(dto.texto());

        if (dto.idOcorrencia() != null) {
            Ocorrencia o = ocorrenciaRepository.findById(dto.idOcorrencia())
                    .orElseThrow(() -> new NotFoundException("Ocorrência não encontrada"));
            existente.setOcorrencia(o);
        } else {
            existente.setOcorrencia(null);
        }

        return toResponse(recomendacaoIARepository.save(existente));
    }
}