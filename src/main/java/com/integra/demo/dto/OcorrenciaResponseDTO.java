package com.integra.demo.dto;

import java.time.LocalDate;

public record OcorrenciaResponseDTO(
        Long idOcorrencia,
        String titulo,
        LocalDate data,
        String descricao,
        String categoria,
        Long idProfessor,
        Long idAluno
) {}